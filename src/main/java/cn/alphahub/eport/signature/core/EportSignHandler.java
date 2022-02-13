package cn.alphahub.eport.signature.core;

import cn.alphahub.eport.signature.config.InitialConfig;
import cn.alphahub.eport.signature.config.UkeyProperties;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.entity.UkeyResponse;
import cn.alphahub.eport.signature.entity.WebSocketWrapper;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;


/**
 * <p>电子口岸加签业务核心类</p>
 * <a href='http://tool.qdhuaxun.cn/?signdoc'>华讯云业务帮助文档，签名的不要看，本项目搞定了所有的签名，华讯的加签便一文不值</a>
 *
 * @author weasley
 * @version 1.0
 * @date 2022/2/12
 */
@Slf4j
@Service
@Validated
public class EportSignHandler {

    /**
     * 电子口岸u-key的配置参数
     */
    private final UkeyProperties ukeyProperties = SpringUtil.getBean(UkeyProperties.class);
    /**
     * StandardWebSocketClient
     */
    private final StandardWebSocketClient standardWebSocketClient = SpringUtil.getBean(StandardWebSocketClient.class);
    /**
     * X509Certificate证书策略
     */
    private final X509CertificateHandler x509CertificateHandler = SpringUtil.getBean(X509CertificateHandler.class);
    @Resource
    private InitialConfig config;

    /**
     * 如果.cer证书不存在时动态切换发送u-key的签名方法
     *
     * @return 发送u-key的签名的入参
     */
    public String getDynamicSignDataParameter(SignRequest request) {
        return config.getIsCertFileExist() ? InitialConfig.getSignDataAsPEMParameter(request) : InitialConfig.getSignDataNoHashAsPEMParameter(request);
    }

    /**
     * WebSocket发送消息给电子口岸u-key
     *
     * @param request 加签请求参数
     * @param payload websocket发送的数据载荷
     */
    public SignResult sign(@Valid SignRequest request, @NotBlank(message = "websocket发送的数据载荷不能为空") String payload) {
        log.info("\nukey入参: \n{}; \r\n加签请求xml原文: \n{}", payload, request.getSourceXml());

        WebSocketWrapper socketWrapper = new WebSocketWrapper();
        socketWrapper.setPayload(payload);
        socketWrapper.setRequest(request);
        socketWrapper.setThreadAtomicReference(new AtomicReference<>(Thread.currentThread()));
        socketWrapper.getSignResult().setDigestValue(SignatureHandler.getDigestValueOfCEBXxxMessage(request.getSourceXml()));

        WebSocketConnectionManager manager = initWebSocketConnectionManager(socketWrapper);
        manager.start();

        try {
            LockSupport.parkNanos(socketWrapper.getThreadAtomicReference(), 1000 * 1000 * 1000 * 5L);
        } catch (Exception e) {
            socketWrapper.getSignResult().setSuccess(false);
            manager.stop();
            log.error("线程自动unpark异常 {}", e.getLocalizedMessage(), e);
        }

        if (manager.isRunning()) {
            manager.stop();
        }

        return socketWrapper.getSignResult();
    }

    /**
     * 初始化WebSocketConnectionManager
     *
     * @param socketWrapper WebSocket包装类
     * @return WebSocketConnectionManager
     */
    private WebSocketConnectionManager initWebSocketConnectionManager(WebSocketWrapper socketWrapper) {
        return new WebSocketConnectionManager(standardWebSocketClient, new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                super.afterConnectionEstablished(session);
                log.warn("已和[{}]建立websocket连接...", ukeyProperties.getWsUrl());
                session.sendMessage(new TextMessage(socketWrapper.getPayload()));
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                super.handleTextMessage(session, message);
                log.warn("收到ukey响应数据: {}", message.getPayload());
                UkeyResponse response = JSONUtil.toBean(message.getPayload(), new TypeReference<>() {
                }, true);
                if (Objects.equals(response.get_id(), socketWrapper.getRequest().getId())) {
                    try {
                        UkeyResponse.Args responseArgs = response.get_args();
                        if (responseArgs.getResult().equals(true) && CollectionUtils.isNotEmpty(responseArgs.getData())) {
                            socketWrapper.getSignResult().setSuccess(true);
                            socketWrapper.getSignResult().setX509Certificate(x509CertificateHandler.get509Certificate(response.get_method()));
                            socketWrapper.getSignResult().setSignatureValue(responseArgs.getData().get(0));
                            socketWrapper.getSignResult().setCertNo(responseArgs.getData().get(1));
                            log.warn("\n收到ukey加签响应结果：{}", JSONUtil.toJsonStr(responseArgs));
                        }
                    } catch (Exception e) {
                        socketWrapper.getSignResult().setSuccess(false);
                        log.error("唤醒线程异常 {}", e.getLocalizedMessage(), e);
                    } finally {
                        LockSupport.unpark(socketWrapper.getThreadAtomicReference().get());
                    }
                }
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
                super.afterConnectionClosed(session, status);
                log.info("WebSocket connection has been closed, Close Status: {}, reason: {}", status.getCode(), status.getReason());
            }

        }, ukeyProperties.getWsUrl());
    }
}
