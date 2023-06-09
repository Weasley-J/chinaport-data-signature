package cn.alphahub.eport.signature.core;

import cn.alphahub.eport.signature.config.EmailProperties;
import cn.alphahub.eport.signature.config.UkeyProperties;
import cn.alphahub.eport.signature.entity.UkeyResponse;
import cn.alphahub.eport.signature.entity.WebSocketWrapper;
import cn.alphahub.multiple.email.EmailTemplate;
import cn.alphahub.multiple.email.annotation.Email;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
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

    @Autowired
    private EmailTemplate emailTemplate;

    @Autowired
    private EmailProperties emailProperties;

    public WebSocketClientHandler(UkeyProperties ukeyProperties, WebSocketWrapper webSocketWrapper, CertificateHandler certificateHandler) {
        this.ukeyProperties = ukeyProperties;
        this.webSocketWrapper = webSocketWrapper;
        this.certificateHandler = certificateHandler;
    }

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
                        webSocketWrapper.getSignResult().setX509Certificate(certificateHandler.getX509Certificate(response.get_method()));
                    }
                } else this.handleFailedToProcessSign(JSONUtil.toJsonPrettyStr(response));
            } catch (Exception e) {
                webSocketWrapper.getSignResult().setSuccess(false);
                log.error("唤醒线程异常 {}", e.getLocalizedMessage(), e);
            } finally {
                LockSupport.unpark(webSocketWrapper.getThreadReference().get());
            }
        }
    }

    /**
     * 处理加签失败的逻辑，发送邮件通知，由于u-key自身硬件问题导致的加签失败，如：
     * {"_id":1,"_method":"cus-sec_SpcSignDataAsPEM","_status":"00","_args":{"Result":false,"Data":[],"Error":["[读卡器底层库]复位读卡器失败:错误码=50070","Err:Custom50070"]}}
     *
     * @param cause cause
     * @apiNote [读卡器底层库]复位读卡器失败会自动重启u-key的Windows进程，希望能提升自我容灾机制
     * @since 2023-06-10
     */
    @Email
    public void handleFailedToProcessSign(String cause) {
        if (emailProperties.getEnable().equals(true)) {
            log.warn("电子口岸u-key加签数据失败：{}", cause);
            EmailTemplate.SimpleMailMessageDomain messageDomain = new EmailTemplate.SimpleMailMessageDomain();
            messageDomain.setTo(emailProperties.getTo());
            messageDomain.setCc(StringUtils.split(emailProperties.getCc(), ","));
            messageDomain.setSentDate(LocalDateTime.now());
            messageDomain.setSubject("电子口岸u-key加签失败");
            if (cause.contains("[读卡器底层库]复位读卡器失败")) {
                messageDomain.setText("电子口岸u-key加签失败，原因：\n" + cause + "\n\n如遇：“[读卡器底层库]复位读卡器失败”等错误，请手动重启加签exe客户端程序");
            } else {
                messageDomain.setText("电子口岸u-key加签失败，原因：\n" + cause);
            }
            emailTemplate.send(messageDomain);
        }
    }
}
