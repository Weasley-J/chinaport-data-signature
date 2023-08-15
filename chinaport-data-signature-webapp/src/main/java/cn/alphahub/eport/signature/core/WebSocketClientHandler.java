package cn.alphahub.eport.signature.core;

import cn.alphahub.dtt.plus.util.JacksonUtil;
import cn.alphahub.eport.signature.config.EmailProperties;
import cn.alphahub.eport.signature.config.UkeyAccessClientProperties.Command;
import cn.alphahub.eport.signature.config.UkeyHealthHelper;
import cn.alphahub.eport.signature.config.UkeyProperties;
import cn.alphahub.eport.signature.entity.ConsoleOutput;
import cn.alphahub.eport.signature.entity.UkeyResponse;
import cn.alphahub.eport.signature.entity.WebSocketWrapper;
import cn.alphahub.multiple.email.EmailTemplate;
import cn.alphahub.multiple.email.EmailTemplate.SimpleMailMessageDomain;
import cn.alphahub.multiple.email.annotation.Email;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
@Slf4j
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WebSocketClientHandler extends TextWebSocketHandler {
    /**
     * 电子口岸u-key的配置参数
     */
    private final UkeyProperties ukeyProperties;
    /**
     * WebSocket包装类
     */
    private final WebSocketWrapper webSocketWrapper;
    /**
     * X509Certificate证书判断
     */
    private final CertificateHandler certificateHandler;
    @Autowired
    private EmailTemplate emailTemplate;
    @Autowired
    private EmailProperties emailProperties;
    @Autowired
    private UkeyHealthHelper ukeyHealthHelper;

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
        UkeyResponse ukeyResponse = JSONUtil.toBean(message.getPayload(), new TypeReference<>() {
        }, true);
        if (Objects.equals(ukeyResponse.get_id(), webSocketWrapper.getRequest().getId())) {
            try {
                UkeyResponse.Args responseArgs = ukeyResponse.get_args();
                if (responseArgs.getResult().equals(true) && CollectionUtils.isNotEmpty(responseArgs.getData())) {
                    log.warn("电子口岸u-key加签数据成功：{}", JacksonUtil.toJson(responseArgs));
                    webSocketWrapper.getSignResult().setSuccess(true);
                    webSocketWrapper.getSignResult().setSignatureValue(responseArgs.getData().get(0));
                    webSocketWrapper.getSignResult().setCertNo(responseArgs.getData().get(1));
                    if (SignHandler.isSignXml(webSocketWrapper.getRequest())) {
                        webSocketWrapper.getSignResult().setX509Certificate(certificateHandler.getX509Certificate(ukeyResponse.get_method()));
                    }
                } else {
                    sendAlertSignFailure(ukeyResponse, message.getPayload());
                }
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
     * <pre>
     * {
     *   "_id": 1,
     *   "_method": "cus-sec_SpcSignDataAsPEM",
     *   "_status": "00",
     *   "_args": {
     *     "Result": false,
     *     "Data": [],
     *     "Error": [
     *       "[读卡器底层库]复位读卡器失败:错误码：50070",
     *       "Err:Custom50070"
     *     ]
     *   }
     * }
     * </pre>
     * <p>
     * <figure>
     * <img src="https://weasley.oss-cn-shanghai.aliyuncs.com/Photos/iShot_2023-08-10_13.56.34.png" alt="邮件通知效果">
     * <figcaption>加签失败邮件通知</figcaption>
     * </figure>
     * </p>
     *
     * @param ukeyResponse   error response
     * @param messagePayload 消息负载
     * @implNote [读卡器底层库]复位读卡器失败会自动重启u-key的Windows进程，希望能提升自我容灾机制
     * @since 2023-06-10
     */
    @Email
    public void sendAlertSignFailure(UkeyResponse ukeyResponse, String messagePayload) {
        String errorMessage = JacksonUtil.toPrettyJson(ukeyResponse);
        String subject = "电子口岸 u-key 加签失败提醒";
        String payload = StringUtils.defaultIfBlank(messagePayload, "");
        if (emailProperties.getEnable().equals(true)) {
            log.warn("电子口岸u-key加签数据失败：{}", errorMessage);
            SimpleMailMessageDomain message = new SimpleMailMessageDomain();
            message.setTo(emailProperties.getTo());
            message.setCc(StringUtils.split(emailProperties.getCc(), ","));
            message.setSentDate(LocalDateTime.now());
            message.setSubject(subject);
            message.setText("");
            boolean shouldBreak = false;
            boolean existsError = false;
            if (CollectionUtils.isNotEmpty(ukeyResponse.get_args().getError())) {
                for (String errorText : ukeyResponse.get_args().getError()) {
                    for (UkeyError ukeyError : UkeyError.values()) {
                        if (StringUtils.containsIgnoreCase(errorText, ukeyError.getErr())) {
                            restartUkeyWindowsWebsocketClient(message);
                            shouldBreak = true;
                            existsError = true;
                            break;
                        }
                    }
                    if (shouldBreak) {
                        break;
                    }
                }
            }
            StringBuilder text = new StringBuilder()
                    .append("原始错误：\n").append(errorMessage).append("\n")
                    .append("数据载荷：\n").append(payload).append("\n");
            if (existsError) {
                message.setText(text.append("提示，如遇 “[读卡器底层库]复位读卡器失败” 等错误，程序自动重启客户端后如果还是不能加签，请手动重启加签exe客户端程序。").toString());
            } else {
                message.setText(text.toString());
            }
            emailTemplate.send(message);
        }
    }

    /**
     * 处理加签失败的逻辑，重启u-key的Windows客户端，由于u-key自身硬件问题导致的加签失败
     *
     * @since 1.0.9
     */
    public void restartUkeyWindowsWebsocketClient(SimpleMailMessageDomain message) {
        ConsoleOutput output = ukeyHealthHelper.fixUkey(Command.RESTART);
        String restartLog = message.getText()
                .concat("\n\n加签程序【chinaport-data-signature】重启Windows Websocket客户端，cmd终端信息:\n")
                .concat(StringUtils.defaultIfBlank(JacksonUtil.toJson(output), ""));
        message.setText(restartLog);
    }

    /**
     * 电子口岸 u-key 发生错误，需要重启“客户端控件”的情况
     */
    @Getter
    @AllArgsConstructor
    private enum UkeyError {
        RESETTING_CARD_READER_FAILED(50070, "[读卡器底层库]复位读卡器失败:错误码=50070", "Custom50070"),
        OPEN_THE_CARD_READER_FAILED(50200, "[读卡器底层库]打开读卡器失败：错误码=50200", "Custom50200"),
        ;
        /**
         * 错误码
         */
        private final int code;
        /**
         * 描述
         */
        private final String desc;
        /**
         * Err
         */
        private final String err;
    }

}
