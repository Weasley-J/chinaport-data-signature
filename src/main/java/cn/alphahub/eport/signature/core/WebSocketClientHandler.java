package cn.alphahub.eport.signature.core;

import cn.alphahub.eport.signature.config.UkeyProperties;
import cn.alphahub.eport.signature.entity.UkeyResponse;
import cn.alphahub.eport.signature.entity.WebSocketWrapper;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;
import java.util.concurrent.locks.LockSupport;

/**
 * 加签websocket客户端基类
 * <ul><li>全参数构造函数注入IOC</li></ul>
 *
 * @author weasley
 * @version 1.0
 * @date 2022/2/15
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WebSocketClientHandler extends TextWebSocketHandler {
    /**
     * 电子口岸u-key的配置参数
     */
    private UkeyProperties ukeyProperties;
    /**
     * WebSocket包装类
     */
    private WebSocketWrapper webSocketWrapper;
    /**
     * X509Certificate证书判断
     */
    private CertificateHandler certificateHandler;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.warn("已和[{}]建立websocket连接...", ukeyProperties.getWsUrl());
        session.sendMessage(new TextMessage(webSocketWrapper.getPayload()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        log.warn("收到ukey响应数据: {}", message.getPayload());
        UkeyResponse response = JSONUtil.toBean(message.getPayload(), new TypeReference<>() {
        }, true);
        if (Objects.equals(response.get_id(), webSocketWrapper.getRequest().getId())) {
            try {
                UkeyResponse.Args responseArgs = response.get_args();
                if (responseArgs.getResult().equals(true) && CollectionUtils.isNotEmpty(responseArgs.getData())) {
                    log.warn("电子口岸u-key加签数据成功：{}", JSONUtil.toJsonStr(responseArgs));
                    webSocketWrapper.getSignResult().setSuccess(true);
                    webSocketWrapper.getSignResult().setSignatureValue(responseArgs.getData().get(0));
                    webSocketWrapper.getSignResult().setCertNo(responseArgs.getData().get(1));
                    if (SignHandler.isSignXml(webSocketWrapper.getRequest())) {
                        webSocketWrapper.getSignResult().setX509Certificate(certificateHandler.get509Certificate(response.get_method()));
                    }
                }
            } catch (Exception e) {
                webSocketWrapper.getSignResult().setSuccess(false);
                log.error("唤醒线程异常 {}", e.getLocalizedMessage(), e);
            } finally {
                LockSupport.unpark(webSocketWrapper.getThreadReference().get());
            }
        }
    }

}
