package cn.alphahub.eport.signature.config;

import cn.alphahub.eport.signature.core.CertificateHandler;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.core.SignatureHandler;
import cn.alphahub.eport.signature.core.WebSocketClientHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SpcValidTime;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.alphahub.eport.signature.entity.UkeyResponse;
import cn.alphahub.eport.signature.entity.WebSocketWrapper;
import cn.alphahub.eport.signature.util.SysUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

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
@EnableConfigurationProperties({UkeyProperties.class, EmailProperties.class})
public class InitialConfig implements ApplicationRunner {
    /**
     * jackson序列化处禁止换成其他json序列化工具
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * u-key默认密码8个8不要修改
     */
    private static final String DEFAULT_PASSWORD = "88888888";

    /* *********************** 获取入参方法开始,已下方法的返回值最为参数连接好海关u-key通过socket实例发送给[ws://127.0.0.1:61232] *********************** */

    /**
     * 取海关签名证书PEM, X509Certificate证书（未经HASH算法散列过）
     *
     * @return 取海关签名证书PEM, X509Certificate证书是加签证书，真正的X509Certificate证书
     */
    @SneakyThrows
    public static String getX509CertificateParameter() {
        UkeyRequest ukeyRequest = new UkeyRequest();
        ukeyRequest.set_method("cus-sec_SpcGetSignCertAsPEM");
        ukeyRequest.set_id(1);
        ukeyRequest.setArgs(new HashMap<>());
        return MAPPER.writeValueAsString(ukeyRequest);
    }

    /**
     * 使用卡计算摘要,返回PEM格式信息, 这个不调也罢，已经硬核计算出来了
     *
     * @param sourceXml 不含ds:Signature节点的<ceb:CEBXxxMessage></ceb:CEBXxxMessage>源xml原文数据
     * @param uniqueId  uniqueId  唯一id, 用来区分是哪一次发送的消息，int32，最大32位大于0
     * @return DigestValue的值
     */
    @SneakyThrows
    public static String getDigestValueParameter(String sourceXml, Integer uniqueId) {
        Map<String, Object> args = new LinkedHashMap<>(2);
        args.put("szInfo", sourceXml);
        args.put("passwd", ObjectUtils.defaultIfNull(SpringUtil.getBean(UkeyProperties.class).getPassword(), DEFAULT_PASSWORD));
        UkeyRequest ukeyRequest = new UkeyRequest();
        ukeyRequest.set_method("sec_SpcSHA1DigestAsPEM");
        ukeyRequest.set_id(uniqueId);
        ukeyRequest.setArgs(args);
        return MAPPER.writeValueAsString(ukeyRequest);
    }

    /**
     * 签名, 不对原文计算摘要, 请您自行计算好摘要传入
     *
     * @param request 原文入参
     * @return SignatureValue的值, 返回的数组，包含您的证书编号，可作为KeyName的值
     */
    @SneakyThrows
    public static String getSignDataAsPEMParameter(SignRequest request) {
        Map<String, Object> args = new LinkedHashMap<>(2);
        String initData = SignHandler.getInitData(request);
        log.warn("发送给ukey的真正请求入参: {}, 原始报文:\n{}", initData, request.getData());
        args.put("inData", initData);
        args.put("passwd", ObjectUtils.defaultIfNull(SpringUtil.getBean(UkeyProperties.class).getPassword(), DEFAULT_PASSWORD));
        UkeyRequest ukeyRequest = new UkeyRequest();
        ukeyRequest.set_method(CertificateHandler.METHOD_OF_X509_WITH_HASH);
        ukeyRequest.set_id(request.getId());
        ukeyRequest.setArgs(args);
        return MAPPER.writeValueAsString(ukeyRequest);
    }

    /**
     * 签名，对原文计算摘要(方法内部已经计算好)
     *
     * @param request CEBXxxMessage加签请求参数
     * @return SignatureValue的值
     */
    @SneakyThrows
    public static String getSignDataNoHashAsPEMParameter(SignRequest request) {
        String initData = SignHandler.getInitData(request);
        //对原文计算摘：SHA-1 digest as a hex string
        String sha1Hex = DigestUtils.sha1Hex(initData);
        log.warn("发送给ukey的真正请求入参: {}, 原始报文:\n{}", sha1Hex, request.getData());
        Map<String, Object> args = new LinkedHashMap<>(2);
        args.put("inData", sha1Hex);
        args.put("passwd", ObjectUtils.defaultIfNull(SpringUtil.getBean(UkeyProperties.class).getPassword(), DEFAULT_PASSWORD));
        UkeyRequest ukeyRequest = new UkeyRequest();
        ukeyRequest.set_method(CertificateHandler.METHOD_OF_X509_WITHOUT_HASH);
        ukeyRequest.set_id(request.getId());
        ukeyRequest.setArgs(args);
        return MAPPER.writeValueAsString(ukeyRequest);
    }

    /**
     * 验证签名,不对原文计算摘要,请您自行计算好摘要传入
     *
     * @param sourceXml      不带ds:Signature节点的CEBXxxMessage.xml原文
     * @param signatureValue 签名信息
     * @param certDataPEM    签名证书,PEM编码格式 可以为空,则取当前插着的卡中的证书
     * @param uniqueId       uniqueId 唯一id, 用来区分是哪一次发送的消息，int32，最大32位大于0
     */
    @SneakyThrows
    public static String getVerifySignDataNoHashParameter(String sourceXml, String signatureValue, @Nullable String certDataPEM, Integer uniqueId) {
        Map<String, Object> argsMap = new LinkedHashMap<>(2);
        //对原文计算摘：SHA-1 digest as a hex string
        String sha1Hex = DigestUtils.sha1Hex(SignatureHandler.getSignatureValueBeforeSend(new SignRequest(null, sourceXml)));
        argsMap.put("inData", sha1Hex);
        argsMap.put("signData", signatureValue);
        if (StringUtils.isNotBlank(certDataPEM)) {
            argsMap.put("certDataPEM", certDataPEM);
        }
        UkeyRequest ukeyRequest = new UkeyRequest();
        ukeyRequest.set_method("cus-sec_SpcVerifySignDataNoHash");
        ukeyRequest.set_id(uniqueId);
        ukeyRequest.setArgs(argsMap);
        return MAPPER.writeValueAsString(ukeyRequest);
    }

    /**
     * 查看海关证书有效期入参
     */
    public String getCertValidTimeParams() {
        Map<String, Object> args = new LinkedHashMap<>();
        //args.put("cert", null);//证书内容,可为空
        UkeyRequest ukeyRequest = new UkeyRequest("cus-sec_SpcGetValidTimeFromCert", 1, args);
        JSONConfig jsonConfig = new JSONConfig();
        return JSONUtil.toJsonStr(ukeyRequest, jsonConfig);
    }

    /* *********************** 获取入参方法结束（这几个方法值5000RMB，小心修改） *********************** */

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
        Map<String, String> x509Map = new ConcurrentHashMap<>(2);

        //使用类加载器{Thread.currentThread().getContextClassLoader().getResourceAsStream(ukeyProperties.getCertPath())}读取打包成jar之后resources下的文件
        ClassPathResource resource = new ClassPathResource("/" + ukeyProperties.getCertPath());
        if (StringUtils.isNotBlank(ukeyProperties.getCertPath()) && resource.exists() || resource.isFile() && resource.isReadable()) {
            String certificateWithHash = IoUtil.readUtf8(resource.getInputStream());
            certificateWithHash = certificateWithHash.replace("-----BEGIN CERTIFICATE-----", "");
            certificateWithHash = certificateWithHash.replace("-----END CERTIFICATE-----", "");
            if (StringUtils.startsWith(certificateWithHash, SysUtil.getLineSeparator())) {
                certificateWithHash = StringUtils.removeStart(certificateWithHash, SysUtil.getLineSeparator());
            }
            if (certificateWithHash.endsWith(SysUtil.getLineSeparator())) {
                certificateWithHash = StringUtils.substring(certificateWithHash, 0, certificateWithHash.length() - SysUtil.getLineSeparator().length());
            }
            x509Map.put(CertificateHandler.METHOD_OF_X509_WITH_HASH, certificateWithHash);
            certificateHandler.setCertExists(true);
            certificateHandler.setX509Map(x509Map);
            if (log.isWarnEnabled()) {
                log.warn("METHOD_OF_X509_WITH_HASH:\n{}", certificateWithHash);
            }
        }

        AtomicReference<Thread> reference = new AtomicReference<>();
        reference.set(Thread.currentThread());

        //获取u-key内的证书相关
        WebSocketConnectionManager certManager = new WebSocketConnectionManager(standardWebSocketClient, new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                session.sendMessage(new TextMessage(InitialConfig.getX509CertificateParameter()));
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) {
                UkeyResponse response = JSONUtil.toBean(message.getPayload(), new TypeReference<>() {
                }, true);
                try {
                    if (Objects.equals(response.get_id(), 1)) {
                        UkeyResponse.Args responseArgs = response.get_args();
                        if (responseArgs.getResult().equals(true) && CollectionUtils.isNotEmpty(responseArgs.getData())) {
                            x509Map.put(CertificateHandler.METHOD_OF_X509_WITHOUT_HASH, responseArgs.getData().get(0));
                            log.warn("已从电子口岸u-key中获取到未经hash算法的x509Certificate证书: {}", MAPPER.writeValueAsString(x509Map));
                        }
                    }
                } catch (Exception e) {
                    log.error("唤醒线程异常 {}", e.getLocalizedMessage(), e);
                } finally {
                    LockSupport.unpark(reference.get());
                }
            }
        }, ukeyProperties.getWsUrl());
        certManager.start();
        try {
            LockSupport.parkNanos(reference.get(), 1000 * 1000 * 1000 * 3L);
        } catch (Exception e) {
            log.error("线程自动unpark异常 {}", e.getLocalizedMessage(), e);
        } finally {
            certManager.stop();
        }

        //获取u-key签发日期相关
        WebSocketConnectionManager certValidTimeManager = new WebSocketConnectionManager(standardWebSocketClient, new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                session.sendMessage(new TextMessage(getCertValidTimeParams()));
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) {
                UkeyResponse response = JSONUtil.toBean(message.getPayload(), new TypeReference<>() {
                }, true);
                try {
                    if (Objects.equals(response.get_id(), 1)) {
                        UkeyResponse.Args responseArgs = response.get_args();
                        if (responseArgs.getResult().equals(true) && CollectionUtils.isNotEmpty(responseArgs.getData())) {
                            log.warn("从电子口岸u-key中获取到u-key有效期区间: {}", responseArgs.getData());
                            SpcValidTime validTime = JSONUtil.toBean(responseArgs.getData().get(0), new TypeReference<>() {
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
        certValidTimeManager.start();
        try {
            LockSupport.parkNanos(reference.get(), 1000 * 1000 * 1000 * 3L);
        } catch (Exception e) {
            log.error("线程自动unpark异常 {}", e.getLocalizedMessage(), e);
        } finally {
            certValidTimeManager.stop();
        }

        certificateHandler.setX509Map(x509Map);
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


