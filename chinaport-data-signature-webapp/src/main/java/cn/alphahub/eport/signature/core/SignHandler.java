package cn.alphahub.eport.signature.core;

import cn.alphahub.eport.signature.config.UkeyInitialConfig;
import cn.alphahub.eport.signature.config.UkeyProperties;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.alphahub.eport.signature.entity.UkeyResponse;
import cn.alphahub.eport.signature.entity.UkeyResponse.Args;
import cn.alphahub.eport.signature.entity.UkeyResponseArgsWrapper;
import cn.alphahub.eport.signature.entity.WebSocketWrapper;
import cn.alphahub.eport.signature.support.XMLValidator;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
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
 * <a href='http://tool.qdhuaxun.cn/?signdoc'>华讯云业务帮助文档，签名的不要看误导人，华讯的业务文档可以参考</a>
 *
 * @author weasley
 * @version 1.0
 * @date 2022/2/12
 */
@Data
@Slf4j
@Service
@Validated
@Accessors(chain = true)
public class SignHandler {
    @Autowired
    private UkeyProperties ukeyProperties;
    @Autowired
    private WebSocketClientHandler webSocketClientHandler;
    @Autowired
    private StandardWebSocketClient standardWebSocketClient;

    /**
     * 获取ukey的socket入参的inData字段
     *
     * @param request 加签数据请求入参
     * @return ukey的socket入参的inData字段
     */
    public static String getInitData(@Valid SignRequest request) {
        if (isSignXml(request)) {
            return SignatureHandler.getSignatureNodeBeforeSend(request);
        }
        return request.getData();
    }

    /**
     * 判断是否总署xml
     *
     * @param request 加签数据请求入参
     * @return 总署xml返回true
     */
    public static boolean isSignXml(@Valid SignRequest request) {
        if (StringUtils.isBlank(request.getData())) {
            throw new IllegalArgumentException("加签数据请求入参能为空!");
        }
        boolean isSignXmlString = XMLValidator.isValidXML(request.getData()) || StringUtils.startsWith(request.getData(), "<ceb:CEB");
        boolean isSign179String = StringUtils.startsWith(request.getData(), "\"sessionID\"");
        if (isSignXmlString && !isSign179String) {
            return true;
        }
        if (isSign179String && !isSignXmlString) {
            return false;
        }
        throw new IllegalArgumentException("加签数据请求入参不合法,请检查参数:" + request.getData());
    }

    /**
     * 获取u-key签名参数
     *
     * @return 发送u-key的签名的入参
     * @since 2022-11-27
     */
    public String getSignDataParameter(@Valid SignRequest request) {
        return UkeyInitialConfig.getSignDataAsPEM(request);
    }

    /**
     * WebSocket发送消息给电子口岸u-key
     *
     * @param request 加签请求参数
     * @param payload websocket发送的数据载荷
     */
    public SignResult sign(@Valid SignRequest request, @NotBlank(message = "websocket发送的数据载荷不能为空") String payload) {
        log.info("收到u-key加签数据: {}", payload);

        WebSocketWrapper wrapper = webSocketClientHandler.getWebSocketWrapper();
        wrapper.setPayload(payload);
        wrapper.setRequest(request);
        wrapper.setSignResult(new SignResult());
        wrapper.setThreadReference(new AtomicReference<>(Thread.currentThread()));
        if (SignHandler.isSignXml(request)) {
            wrapper.getSignResult().setDigestValue(SignatureHandler.getDigestValueOfCEBXxxMessage(request.getData()));
            wrapper.getSignResult().setSignatureNode(SignatureHandler.getSignatureNodeBeforeSend(request));
        }

        WebSocketConnectionManager manager = new WebSocketConnectionManager(standardWebSocketClient, webSocketClientHandler, ukeyProperties.getWsUrl());
        manager.start();

        try {
            LockSupport.parkNanos(wrapper.getThreadReference().get(), 1000 * 1000 * 1000 * 5L);
        } catch (Exception e) {
            log.error("线程自动unpark异常 {}", e.getLocalizedMessage(), e);
            wrapper.getSignResult().setSuccess(false);
        } finally {
            manager.stop();
        }

        return wrapper.getSignResult();
    }

    /**
     * 获取u-key加签返回内层对象（我们要的数据在这里面）
     *
     * @apiNote 发送任意数据给ukey，实时响应结果
     */
    public Args getUkeyResponseArgs(UkeyRequest ukeyRequest) {
        AtomicReference<UkeyResponseArgsWrapper> reference = new AtomicReference<>();
        reference.set(new UkeyResponseArgsWrapper(Thread.currentThread(), new Args()));
        WebSocketConnectionManager webSocketConnectionManager = new WebSocketConnectionManager(standardWebSocketClient, new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                session.sendMessage(new TextMessage(JSONUtil.toJsonStr(ukeyRequest)));
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) {
                UkeyResponse response = JSONUtil.toBean(message.getPayload(), new TypeReference<>() {
                }, true);
                try {
                    if (Objects.equals(response.get_id(), ukeyRequest.get_id())) {
                        log.warn("从电子口岸ukey中获取获取到数据: {}", response.get_args());
                        reference.get().setResponseArgs(response.get_args());
                    }
                } catch (Exception e) {
                    log.error("唤醒线程异常 {}", e.getLocalizedMessage(), e);
                }
            }
        }, ukeyProperties.getWsUrl());
        webSocketConnectionManager.start();
        try {
            LockSupport.parkNanos(reference.get().getThread(), 1000 * 1000 * 1000 * 1L);
        } catch (Exception e) {
            log.error("线程自动unpark异常 {}", e.getLocalizedMessage(), e);
        } finally {
            webSocketConnectionManager.stop();
        }
        return reference.get().getResponseArgs();
    }

}
