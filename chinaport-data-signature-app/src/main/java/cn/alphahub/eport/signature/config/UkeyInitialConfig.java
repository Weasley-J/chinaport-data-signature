package cn.alphahub.eport.signature.config;

import cn.alphahub.dtt.plus.util.JacksonUtil;
import cn.alphahub.dtt.plus.util.SpringUtil;
import cn.alphahub.eport.signature.core.CertificateHandler;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.core.WebSocketClientHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SpcValidTime;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.alphahub.eport.signature.entity.UkeyResponse;
import cn.alphahub.eport.signature.entity.WebSocketWrapper;
import cn.alphahub.eport.signature.support.CertificateParser;
import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

import static cn.alphahub.eport.signature.core.CertificateHandler.SING_DATA_METHOD;
import static cn.hutool.json.JSONUtil.toBean;
import static cn.hutool.json.JSONUtil.toJsonStr;

/**
 * 初始化配置
 *
 * @author lwj
 * @version 1.0
 * @date 2022-01-11 15:29
 */
@Data
@Slf4j
@Configuration
@EnableConfigurationProperties({UkeyProperties.class, EmailProperties.class, SignatureAlgorithmProperties.class})
public class UkeyInitialConfig implements ApplicationRunner {
    /**
     * u-key默认密码8个8不要修改
     */
    private static final String DEFAULT_PASSWORD = "88888888";

    /* *********************** 已下方法的返回值最为参数连接好海关u-key通过socket实例发送给[ws://127.0.0.1:61232] *********************** */

    /**
     * 取海关签名证书PEM, X509Certificate证书（未经HASH算法散列过）
     *
     * @return 取海关签名证书PEM, X509Certificate证书是加签证书，真正的X509Certificate证书
     */
    public static String getX509CertificateParameter() {
        UkeyRequest ukeyRequest = new UkeyRequest();
        ukeyRequest.set_method("cus-sec_SpcGetSignCertAsPEM");
        ukeyRequest.set_id(1);
        ukeyRequest.setArgs(new HashMap<>());
        return JacksonUtil.toJson(ukeyRequest);
    }

    /**
     * 使用卡计算摘要,返回PEM格式信息, 这个不调也罢，已经硬核计算出来了
     *
     * @param sourceXml 不含ds:Signature节点的<ceb:CEBXxxMessage></ceb:CEBXxxMessage>源xml原文数据
     * @param uniqueId  uniqueId  唯一id, 用来区分是哪一次发送的消息，int32，最大32位大于0
     * @return DigestValue的值
     */
    public static String getDigestValueParameter(String sourceXml, Integer uniqueId) {
        Map<String, Object> args = new LinkedHashMap<>(2);
        args.put("szInfo", sourceXml);
        args.put("passwd", ObjectUtils.defaultIfNull(SpringUtil.getBean(UkeyProperties.class).getPassword(), DEFAULT_PASSWORD));
        UkeyRequest ukeyRequest = new UkeyRequest();
        ukeyRequest.set_method("sec_SpcSHA1DigestAsPEM");
        ukeyRequest.set_id(uniqueId);
        ukeyRequest.setArgs(args);
        return JacksonUtil.toJson(ukeyRequest);
    }

    /**
     * 签名, 不对原文计算摘要, 请您自行计算好摘要传入
     *
     * @param request 原文入参
     * @return SignatureValue的值, 返回的数组，包含您的证书编号，可作为KeyName的值
     */
    public static String getSignDataAsPEM(SignRequest request) {
        String initData = SignHandler.getInitData(request);
        log.warn("发送给ukey的真正请求入参: {}\n原始报文:\n{}", initData, request.getData());
        @SuppressWarnings({"all"}) UkeyRequest ukeyRequest = new UkeyRequest("cus-sec_SpcSignDataAsPEM", new HashMap<>() {{
            put("inData", initData);
            put("passwd", ObjectUtils.defaultIfNull(SpringUtil.getBean(UkeyProperties.class).getPassword(), DEFAULT_PASSWORD));
        }});
        ukeyRequest.set_id(request.getId());
        return JacksonUtil.toJson(ukeyRequest);
    }

    /**
     * 使用卡计算摘要,返回PEM格式信息
     *
     * @return ukey响应数据，示例: B959CD298E3BA6DAE360FB8CDB87B68B66D8BD221474046232910DD5FDA54354
     */
    public static String getSHA1DigestAsPEMParams(SignRequest request) {
        Map<String, Object> args = new LinkedHashMap<>();
        String initData = SignHandler.getInitData(request);
        log.warn("使用卡计算摘要，返回PEM格式信息: \n{}原始报文:\n{}", initData, request.getData());
        args.put("szInfo", initData);
        args.put("passwd", ObjectUtils.defaultIfNull(SpringUtil.getBean(UkeyProperties.class).getPassword(), DEFAULT_PASSWORD));
        UkeyRequest ukeyRequest = new UkeyRequest();
        ukeyRequest.set_method("cus-sec_SpcSHA1DigestAsPEM");
        ukeyRequest.set_id(request.getId());
        ukeyRequest.setArgs(args);
        return JacksonUtil.toJson(ukeyRequest);
    }

    /* *********************** 获取入参方法结束（这几个方法值，小心修改） *********************** */

    /**
     * To park thread
     */
    private static void parkThread(AtomicReference<Thread> reference, WebSocketConnectionManager socketConnectionManager) {
        socketConnectionManager.start();
        try {
            LockSupport.parkNanos(reference.get(), 1000 * 1000 * 1000 * 3L);
        } catch (Exception e) {
            log.error("线程自动unpark异常 {}", e.getLocalizedMessage(), e);
        } finally {
            socketConnectionManager.stop();
        }
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     */
    @Override
    public void run(ApplicationArguments args) throws IOException {
        org.apache.xml.security.Init.init();
        if (log.isDebugEnabled()) {
            log.debug("XMl output format with C14n Initial Succeed.");
        }
    }

    /**
     * IOC中返回一个WebSocket包装类的实例对象
     *
     * @return WebSocket包装类
     */
    @Bean
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebSocketWrapper webSocketWrapper() {
        return new WebSocketWrapper();
    }

    /**
     * 基于标准 Java WebSocket API 的 WebSocketClient
     *
     * @return An instance of StandardWebSocketClient
     */
    @Bean
    @ConditionalOnMissingBean({StandardWebSocketClient.class})
    public StandardWebSocketClient standardWebSocketClient() {
        return new StandardWebSocketClient();
    }

    /**
     * 注入一个X509Certificate证书判断的Bean
     *
     * @return X509Certificate证书判断
     */
    @Bean
    @SneakyThrows
    public CertificateHandler certificateHandler(UkeyProperties ukeyProperties, StandardWebSocketClient standardWebSocketClient) {
        CertificateHandler certificateHandler = new CertificateHandler();

        Map<String, String> certMap = new ConcurrentHashMap<>();
        AtomicReference<Thread> reference = new AtomicReference<>();
        reference.set(Thread.currentThread());

        // 获取u-key内的证书相关
        WebSocketConnectionManager certManager = new WebSocketConnectionManager(standardWebSocketClient, new TextWebSocketHandler() {
            final UkeyRequest ukeyRequest = new UkeyRequest("cus-sec_SpcGetSignCertAsPEM", new HashMap<>());

            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                session.sendMessage(new TextMessage(JacksonUtil.toJson(ukeyRequest)));
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) {
                UkeyResponse response = toBean(message.getPayload(), new TypeReference<>() {
                }, true);
                try {
                    if (Objects.equals(response.get_id(), 1)) {
                        UkeyResponse.Args responseArgs = response.get_args();
                        if (responseArgs.getResult().equals(true) && CollectionUtils.isNotEmpty(responseArgs.getData())) {
                            certMap.put(SING_DATA_METHOD, responseArgs.getData().get(0));
                            log.warn("已从电子口岸u-key中获取到未经hash算法的x509Certificate证书: {}", JacksonUtil.toJson(certMap));
                        }
                    }
                } catch (Exception e) {
                    log.error("唤醒线程异常 {}", e.getLocalizedMessage(), e);
                } finally {
                    LockSupport.unpark(reference.get());
                }
            }
        }, ukeyProperties.getWsUrl());
        parkThread(reference, certManager);

        // 获取u-key签发日期, 查看海关证书有效期
        WebSocketConnectionManager ukeyValidTimeManager = new WebSocketConnectionManager(standardWebSocketClient, new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                session.sendMessage(new TextMessage(toJsonStr(new UkeyRequest("cus-sec_SpcGetValidTimeFromCert", 1, new LinkedHashMap<>()))));
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) {
                UkeyResponse response = toBean(message.getPayload(), new TypeReference<>() {
                }, true);
                try {
                    if (Objects.equals(response.get_id(), 1)) {
                        UkeyResponse.Args responseArgs = response.get_args();
                        if (responseArgs.getResult().equals(true) && CollectionUtils.isNotEmpty(responseArgs.getData())) {
                            log.warn("从电子口岸u-key中获取到u-key有效期区间: {}", responseArgs.getData());
                            SpcValidTime validTime = toBean(responseArgs.getData().get(0), new TypeReference<>() {
                            }, true);
                            certificateHandler.setUkeyValidTimeBegin(validTime.getSzStartTime());
                            certificateHandler.setUkeyValidTimeEnd(validTime.getSzEndTime());
                        }
                    }
                } catch (Exception e) {
                    log.error("唤醒线程异常 {}", e.getLocalizedMessage(), e);
                } finally {
                    LockSupport.unpark(reference.get());
                }
            }
        }, ukeyProperties.getWsUrl());
        parkThread(reference, ukeyValidTimeManager);

        //自动推断ukey使用的签名算法
        WebSocketConnectionManager certSignatureAlgorithmManager = new WebSocketConnectionManager(standardWebSocketClient, new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                log.info("开始推断ukey使用的签名算法:");
                session.sendMessage(new TextMessage(toJsonStr(new UkeyRequest("cus-sec_SpcGetSignCertAsPEM", new LinkedHashMap<>()))));
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) {
                UkeyResponse response = toBean(message.getPayload(), new TypeReference<>() {
                }, true);
                try {
                    if (Objects.equals(response.get_id(), 1)) {
                        UkeyResponse.Args responseArgs = response.get_args();
                        if (responseArgs.getResult().equals(true) && CollectionUtils.isNotEmpty(responseArgs.getData())) {
                            String x509Certificate = CertificateHandler.buildX509CertificateWithHeader(responseArgs.getData().get(0));
                            X509Certificate certificateByCertText = CertificateParser.parseCertificateByCertText(x509Certificate);
                            if (null != SignatureAlgorithm.getSignatureAlgorithmSigAlgName(certificateByCertText.getSigAlgName())) {
                                certificateHandler.setAlgorithm(SignatureAlgorithm.getSignatureAlgorithmSigAlgName(certificateByCertText.getSigAlgName()));
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("唤醒线程异常 {}", e.getLocalizedMessage(), e);
                } finally {
                    LockSupport.unpark(reference.get());
                }
            }
        }, ukeyProperties.getWsUrl());
        parkThread(reference, certSignatureAlgorithmManager);

        certificateHandler.setX509Map(certMap);
        return certificateHandler;
    }

    /**
     * @return An instance of WebSocketClientHandler
     */
    @Bean
    @ConditionalOnMissingBean({WebSocketClientHandler.class})
    public WebSocketClientHandler webSocketClientHandler(UkeyProperties ukeyProperties, WebSocketWrapper webSocketWrapper, CertificateHandler certificateHandler) {
        return new WebSocketClientHandler(ukeyProperties, webSocketWrapper, certificateHandler);
    }

}
