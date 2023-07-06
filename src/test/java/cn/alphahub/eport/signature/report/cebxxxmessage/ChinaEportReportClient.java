package cn.alphahub.eport.signature.report.cebxxxmessage;

import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.report.cebxxxmessage.ChinaEportConfiguration.ChinaEportProperties;
import cn.hutool.core.codec.Base64;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import o.github.weasleyj.eport.signature.AbstractCebMessage;
import o.github.weasleyj.eport.signature.IMessageType;
import o.github.weasleyj.eport.signature.exception.UploadEportException;
import o.github.weasleyj.eport.signature.model.cebmessage.BaseTransfer;
import o.github.weasleyj.eport.signature.model.request.Message;
import o.github.weasleyj.eport.signature.model.request.MessageBody;
import o.github.weasleyj.eport.signature.model.request.MessageHead;
import o.github.weasleyj.eport.signature.model.request.MessageRequest;
import o.github.weasleyj.eport.signature.model.signxmlnode.CanonicalizationMethod;
import o.github.weasleyj.eport.signature.model.signxmlnode.DigestMethod;
import o.github.weasleyj.eport.signature.model.signxmlnode.KeyInfo;
import o.github.weasleyj.eport.signature.model.signxmlnode.Reference;
import o.github.weasleyj.eport.signature.model.signxmlnode.Signature;
import o.github.weasleyj.eport.signature.model.signxmlnode.SignatureMethod;
import o.github.weasleyj.eport.signature.model.signxmlnode.SignedInfo;
import o.github.weasleyj.eport.signature.model.signxmlnode.Transforms;
import o.github.weasleyj.eport.signature.model.signxmlnode.X509Data;
import o.github.weasleyj.eport.signature.util.JAXBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

;


/**
 * 电子口岸上报客户端
 */
@Slf4j
@Service
public class ChinaEportReportClient {

    /**
     * 海关服务器地址格式：http://ip:port
     *
     * @apiNote base64加密下，不适合直接公布到公网
     */
    private final static String EPORT_SERVER_BASE64 = "aHR0cDovLzM2LjEwMS4yMDguMjMwOjgwOTA=";

    @Autowired
    private SignHandler signHandler;

    @Autowired
    private ChinaEportProperties chinaEportProperties;

    /**
     * 数据上报海关
     *
     * @param cebMessage  原始xml对象
     * @param messageType CEB311Message|CEB621Message|...
     * @return 推送结果, OK: 表示成功, 报关结果需要自己查询回执
     */
    public String upload(AbstractCebMessage cebMessage, IMessageType messageType) {
        log.info("数据上报海关xml入参: {}, {}", JSONUtil.toJsonStr(cebMessage), messageType.getMessageType());
        MessageRequest messageRequest = buildMessageRequest(cebMessage, messageType);
        String requestServer = Base64.decodeStr(EPORT_SERVER_BASE64);
        String requestBody = JSONUtil.toJsonStr(messageRequest);
        log.info("数据上报海关请求入参 {}\nRequest Server: {}", requestBody, requestServer);
        HttpResponse response = HttpUtil.createPost(requestServer + "/cebcmsg")
                .contentType(ContentType.JSON.getValue())
                .body(requestBody)
                .execute();
        String responseBody = response.body();
        log.info("数据上报Http响应结果: {}", responseBody);
        if (!"OK".equals(responseBody)) {
            throw new UploadEportException("上报海关请求异常, 请求入参: " + requestBody + "\n海关Http响应: " + responseBody);
        }
        return response.body();
    }

    /**
     * Build BaseTransfer XML Node
     */
    public BaseTransfer buildBaseTransfer() {
        BaseTransfer baseTransfer = new BaseTransfer();
        baseTransfer.setCopCode(chinaEportProperties.getCopCode());
        baseTransfer.setCopName(chinaEportProperties.getCopName());
        baseTransfer.setDxpId(chinaEportProperties.getDxpId());
        baseTransfer.setDxpMode("DXP");
        return baseTransfer;
    }

    /**
     * 组装最终请求数据，完成末三段加密
     */
    protected MessageRequest buildMessageRequest(AbstractCebMessage cebMessage, IMessageType messageType) {
        if (cebMessage == null) {
            return new MessageRequest();
        }
        String xmlDataInfo = JAXBUtil.toXml(cebMessage);
        MessageHead messageHead = new MessageHead();
        messageHead.setMessageType(messageType.getMessageType() + ".xml");
        messageHead.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        xmlDataInfo = xmlDataInfo.replaceAll("xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"", "");

        SignRequest ukeyRequest = new SignRequest();
        ukeyRequest.setId(1);
        ukeyRequest.setData(xmlDataInfo);

        // 请求签名加密，2次组装数据, 请求加密（包含第1，2段加密）
        String payload = signHandler.getDynamicSignDataParameter(ukeyRequest);
        SignResult signed = signHandler.sign(ukeyRequest, payload);

        String xmlBody = buildXml(signed, xmlDataInfo, messageType);
        // 第3次组装数据, 将加签后的 XM 进行 base64 加密
        String encodedXml = Base64.encode(xmlBody);
        MessageBody messageBody = MessageBody.builder().data(encodedXml).build();
        Message message = Message.builder().MessageBody(messageBody).MessageHead(messageHead).build();
        return new MessageRequest(message);
    }

    /**
     * 组装Signature XML节点
     *
     * @param sourceXml 原始XML
     */
    protected String buildXml(SignResult signResult, String sourceXml, IMessageType messageType) {
        Signature signature = Signature.builder()
                .keyInfo(KeyInfo.builder()
                        .keyName(signResult.getCertNo())
                        .x509Data(X509Data.builder().X509Certificate(signResult.getX509Certificate()).build())
                        .build())
                .signatureValue(signResult.getSignatureValue())
                .signedInfo(SignedInfo.builder()
                        .CanonicalizationMethod(new CanonicalizationMethod())
                        .SignatureMethod(new SignatureMethod())
                        .reference(Reference.builder()
                                .digestMethod(new DigestMethod())
                                .uri("")
                                .transforms(new Transforms())
                                .digestValue(signResult.getDigestValue()).build()
                        ).build()
                ).build();
        String signatureNode = JAXBUtil.toXml(signature);
        String xmlStart = "</ceb:" + messageType.getMessageType() + ">";
        int index = sourceXml.indexOf(xmlStart);
        StringBuilder builder = new StringBuilder(sourceXml);
        StringBuilder insert = builder.insert(index, signatureNode.concat("\n"));
        String finalXml = insert.toString();
        if (finalXml.startsWith("<?xml ")) {
            finalXml = finalXml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", "");
        }
        log.info("上报海关的XML报文: \n{}", finalXml);
        return finalXml;
    }

}
