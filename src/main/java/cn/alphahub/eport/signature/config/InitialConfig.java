package cn.alphahub.eport.signature.config;

import cn.alphahub.eport.signature.core.CertificateHandler;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.core.SignatureHandler;
import cn.alphahub.eport.signature.core.WebSocketClientHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.UkeyResponse;
import cn.alphahub.eport.signature.entity.WebSocketWrapper;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.extra.spring.SpringUtil;
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
@EnableConfigurationProperties({UkeyProperties.class})
public class InitialConfig implements ApplicationRunner {

    /**
     * jackson序列化处禁止换成其他json序列化工具
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * ukey默认密码8个8不要修改
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
        Map<String, Object> parameterMap = new LinkedHashMap<>(3);
        parameterMap.put("_method", "cus-sec_SpcGetSignCertAsPEM");
        parameterMap.put("_id", 1);
        parameterMap.put("args", "{}");
        log.warn("{}", MAPPER.writeValueAsString(parameterMap));
        return MAPPER.writeValueAsString(parameterMap);
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
        Map<String, Object> parameterMap = new LinkedHashMap<>(3);
        parameterMap.put("_method", "cus-sec_SpcSHA1DigestAsPEM");
        parameterMap.put("_id", uniqueId);
        parameterMap.put("args", args);
        log.warn("{}", MAPPER.writeValueAsString(parameterMap));
        return MAPPER.writeValueAsString(parameterMap);
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
        log.warn("发送给ukey的真正请求入参: {}\n原始报文: {}", initData, request.getData());
        args.put("inData", initData);
        args.put("passwd", ObjectUtils.defaultIfNull(SpringUtil.getBean(UkeyProperties.class).getPassword(), DEFAULT_PASSWORD));
        Map<String, Object> parameterMap = new LinkedHashMap<>(3);
        parameterMap.put("_method", CertificateHandler.METHOD_OF_X509_WITH_HASH);
        parameterMap.put("_id", request.getId());
        parameterMap.put("args", args);
        log.warn("{}", MAPPER.writeValueAsString(parameterMap));
        return MAPPER.writeValueAsString(parameterMap);
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
        log.warn("发送给ukey的真正请求入参: {}\n原始报文:{}", sha1Hex, request.getData());
        Map<String, Object> args = new LinkedHashMap<>(2);
        args.put("inData", sha1Hex);
        args.put("passwd", ObjectUtils.defaultIfNull(SpringUtil.getBean(UkeyProperties.class).getPassword(), DEFAULT_PASSWORD));
        Map<String, Object> parameterMap = new LinkedHashMap<>(3);
        parameterMap.put("_method", CertificateHandler.METHOD_OF_X509_WITHOUT_HASH);
        parameterMap.put("_id", request.getId());
        parameterMap.put("args", args);
        log.warn("{}", MAPPER.writeValueAsString(parameterMap));
        return MAPPER.writeValueAsString(parameterMap);
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
        Map<String, Object> parameterMap = new LinkedHashMap<>(3);
        parameterMap.put("_method", "cus-sec_SpcVerifySignDataNoHash");
        parameterMap.put("_id", uniqueId);
        parameterMap.put("args", argsMap);
        log.warn("{}", MAPPER.writeValueAsString(parameterMap));
        return MAPPER.writeValueAsString(parameterMap);
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

        CertificateHandler certificateHandler = SpringUtil.getBean(CertificateHandler.class);
        UkeyProperties ukeyProperties = SpringUtil.getBean(UkeyProperties.class);
        if (StringUtils.isBlank(ukeyProperties.getCertPath())) {
            certificateHandler.setCertExists(false);
            return;
        }

        //使用类加载器读取打打包成jar之后resources先的文件
        /*
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(ukeyProperties.getCertPath());
        if (inputStream == null) {
            certificateHandler.setCertExists(false);
            return;
        }
        */

        ClassPathResource resource = new ClassPathResource("/" + ukeyProperties.getCertPath());
        if (resource.exists() || resource.isFile() && resource.isReadable()) {
            String x509CertificateWithHash = IoUtil.readUtf8(resource.getInputStream());
            x509CertificateWithHash = x509CertificateWithHash.replace("-----BEGIN CERTIFICATE-----", "");
            x509CertificateWithHash = x509CertificateWithHash.replace("-----END CERTIFICATE-----", "");
            // Windows
            if (StringUtils.startsWith(x509CertificateWithHash, "\r\n")) {
                x509CertificateWithHash = StringUtils.removeStart(x509CertificateWithHash, "\r\n");
            }
            if (x509CertificateWithHash.startsWith("\n")) {
                x509CertificateWithHash = StringUtils.replaceOnce(x509CertificateWithHash, "\n", "");
            }
            if (x509CertificateWithHash.endsWith("\n\n")) {
                x509CertificateWithHash = StringUtils.substring(x509CertificateWithHash, 0, x509CertificateWithHash.length() - 4);
            }
            if (x509CertificateWithHash.endsWith("\n")) {
                x509CertificateWithHash = StringUtils.substring(x509CertificateWithHash, 0, x509CertificateWithHash.length() - 2);
            }

            certificateHandler.setCertExists(true);
            certificateHandler.getX509Map().put(CertificateHandler.METHOD_OF_X509_WITH_HASH, x509CertificateWithHash);

            log.warn("METHOD_OF_X509_WITH_HASH:\n{}", x509CertificateWithHash);
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
    public CertificateHandler certificateHandler(UkeyProperties properties, StandardWebSocketClient standardWebSocketClient) {
        CertificateHandler certificateHandler = new CertificateHandler();
        Map<String, String> x509Map = new ConcurrentHashMap<>(2);

        AtomicReference<Thread> reference = new AtomicReference<>();
        reference.set(Thread.currentThread());

        WebSocketConnectionManager manager = new WebSocketConnectionManager(standardWebSocketClient, new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                log.warn("已和[{}]建立websocket连接.", properties.getWsUrl());
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
        }, properties.getWsUrl());

        manager.start();

        try {
            LockSupport.parkNanos(reference.get(), 1000 * 1000 * 1000 * 3L);
        } catch (Exception e) {
            log.error("线程自动unpark异常 {}", e.getLocalizedMessage(), e);
        } finally {
            manager.stop();
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

