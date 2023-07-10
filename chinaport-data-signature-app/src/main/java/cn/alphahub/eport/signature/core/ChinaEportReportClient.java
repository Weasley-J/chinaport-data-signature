package cn.alphahub.eport.signature.core;

import cn.alphahub.eport.signature.base.exception.SignException;
import cn.alphahub.eport.signature.config.ChinaEportProperties;
import cn.alphahub.eport.signature.config.UkeyProperties;
import cn.alphahub.eport.signature.entity.Capture179DataRequest;
import cn.alphahub.eport.signature.entity.Capture179DataResponse;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.entity.ThirdAbstractResponse;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.alphahub.eport.signature.entity.UkeyResponse.Args;
import cn.alphahub.eport.signature.entity.UploadCEBMessageRequest;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import o.github.weasleyj.china.eport.sign.AbstractCebMessage;
import o.github.weasleyj.china.eport.sign.IMessageType;
import o.github.weasleyj.china.eport.sign.model.cebmessage.BaseTransfer;
import o.github.weasleyj.china.eport.sign.model.cebmessage.CEB311Message;
import o.github.weasleyj.china.eport.sign.model.cebmessage.CEB621Message;
import o.github.weasleyj.china.eport.sign.model.customs179.Customs179Request;
import o.github.weasleyj.china.eport.sign.model.request.Message;
import o.github.weasleyj.china.eport.sign.model.request.MessageBody;
import o.github.weasleyj.china.eport.sign.model.request.MessageHead;
import o.github.weasleyj.china.eport.sign.model.request.MessageRequest;
import o.github.weasleyj.china.eport.sign.model.signature.CanonicalizationMethod;
import o.github.weasleyj.china.eport.sign.model.signature.DigestMethod;
import o.github.weasleyj.china.eport.sign.model.signature.KeyInfo;
import o.github.weasleyj.china.eport.sign.model.signature.Reference;
import o.github.weasleyj.china.eport.sign.model.signature.Signature;
import o.github.weasleyj.china.eport.sign.model.signature.SignatureMethod;
import o.github.weasleyj.china.eport.sign.model.signature.SignedInfo;
import o.github.weasleyj.china.eport.sign.model.signature.Transforms;
import o.github.weasleyj.china.eport.sign.model.signature.X509Data;
import o.github.weasleyj.china.eport.sign.util.GUIDUtil;
import o.github.weasleyj.china.eport.sign.util.JAXBUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static cn.alphahub.dtt.plus.util.JacksonUtil.toJson;
import static cn.alphahub.eport.signature.core.CertificateHandler.METHOD_OF_X509_WITH_HASH;

/**
 * 电子口岸上报客户端
 *
 * @author weasley
 * @since 1.1.0
 */
@Slf4j
@Component
public class ChinaEportReportClient {

    /**
     * 海关服务器地址，格式: http://ip:port
     *
     * @apiNote base64加密下，不适合直接公布到公网
     */
    public static final String EPORT_CEBMESSAGE_SERVER_ENCODE = "aHR0cDovLzM2LjEwMS4yMDguMjMwOjgwOTA=";
    /**
     * 海关 179 数据抓取测试环境 URL （base64加密，不适合直接公布到外网）
     */
    public static final String REPORT_TEST_ENV_179_URL_ENCODE = "aHR0cHM6Ly9zd2FwcHRlc3Quc2luZ2xld2luZG93LmNuL2NlYjJncmFiL2dyYWIvcmVhbFRpbWVEYXRhVXBsb2Fk";
    /**
     * 海关 179 数据抓取生产环境 URL （base64加密，不适合直接公布到外网）
     */
    public static final String REPORT_PROD_ENV_179_URL_ENCODE = "aHR0cHM6Ly9jdXN0b21zLmNoaW5hcG9ydC5nb3YuY24vY2ViMmdyYWIvZ3JhYi9yZWFsVGltZURhdGFVcGxvYWQ=";
    @Autowired
    private SignHandler signHandler;
    @Autowired
    private CertificateHandler certificateHandler;
    @Autowired
    private UkeyProperties ukeyProperties;
    @Autowired
    private ChinaEportProperties chinaEportProperties;

    /**
     * 推断CebMessage的具体的XML入参解析成Java对象
     */
    public AbstractCebMessage getCebMessageByMessageType(UploadCEBMessageRequest request) {
        String guid = GUIDUtil.getGuid();
        return switch (request.getMessageType()) {
            case CEB311Message -> {
                CEB311Message ceb311Message = Objects.requireNonNull(JAXBUtil.toBean(request.getCebMessage(), CEB311Message.class));
                ceb311Message.setGuid(guid);
                ceb311Message.getOrder().getOrderHead().setGuid(guid);
                buildBaseTransfer(ceb311Message.getBaseTransfer());
                yield ceb311Message;
            }
            case CEB621Message -> {
                CEB621Message ceb621Message = Objects.requireNonNull(JAXBUtil.toBean(request.getCebMessage(), CEB621Message.class));
                ceb621Message.setGuid(guid);
                ceb621Message.getInventory().getInventoryHead().setGuid(guid);
                buildBaseTransfer(ceb621Message.getBaseTransfer());
                yield ceb621Message;
            }
        };
    }

    /**
     * Build BaseTransfer XML Node
     */
    public BaseTransfer buildBaseTransfer(BaseTransfer baseTransfer) {
        baseTransfer.setCopCode(StringUtils.defaultIfBlank(baseTransfer.getCopCode(), chinaEportProperties.getCopCode()));
        baseTransfer.setCopName(StringUtils.defaultIfBlank(baseTransfer.getCopName(), chinaEportProperties.getCopName()));
        baseTransfer.setDxpId(StringUtils.defaultIfBlank(baseTransfer.getDxpId(), chinaEportProperties.getDxpId()));
        baseTransfer.setDxpMode(StringUtils.defaultIfBlank(baseTransfer.getDxpMode(), "DXP"));
        return baseTransfer;
    }

    /**
     * CebXxxMessage数据上报海关
     *
     * @param cebMessage  原始xml对象
     * @param messageType CEB311Message|CEB621Message|...
     * @return 推送结果, OK: 表示成功, 报关结果需要自己查询回执
     */
    public ThirdAbstractResponse<String, String> report(AbstractCebMessage cebMessage, IMessageType messageType) {
        log.info("数据上报海关xml入参: {}, {}", JSONUtil.toJsonStr(cebMessage), messageType.getMessageType());
        MessageRequest messageRequest = buildMessageRequest(cebMessage, messageType);
        String requestServer = Base64.decodeStr(EPORT_CEBMESSAGE_SERVER_ENCODE);
        requestServer = StringUtils.defaultIfBlank(chinaEportProperties.getServer(), requestServer);
        String requestBody = JSONUtil.toJsonStr(messageRequest);
        log.info("数据上报海关请求入参 {}\nRequest Server: {}", requestBody, requestServer);
        HttpResponse httpResponse = HttpUtil.createPost(requestServer + "/cebcmsg")
                .contentType(ContentType.JSON.getValue())
                .body(requestBody)
                .execute();
        String responseBody = httpResponse.body();
        log.info("数据上报Http响应结果: {}", responseBody);
        if (!"OK".equals(responseBody)) {
            log.error("\"上报海关请求异常, 请求入参: \" + requestBody + \"\\n海关Http响应: \" + responseBody");
        }
        ThirdAbstractResponse<String, String> thirdResponse = ThirdAbstractResponse.getInstance();
        thirdResponse.setOriginal(responseBody);
        thirdResponse.setExpected("OK");
        return thirdResponse;
    }

    /**
     * 海关 179 数据抓取
     * <p>
     * <ul>
     *     <li>
     *         期望的成功数据返回示例
     *     <pre>
     *       {"code":"10000","message":"","serviceTime":1567050097628}
     *     </pre>
     *     </li>
     * </ul>
     */
    public ThirdAbstractResponse<String, Capture179DataResponse> capture179Data(Capture179DataRequest request) {
        log.info("海关 179 数据抓取 {}", JSONUtil.toJsonStr(request));
        Customs179Request customs179Request = new Customs179Request();
        customs179Request.setSessionID(request.getSessionID());
        customs179Request.setPayExchangeInfoHead(request.getPayExchangeInfoHead());
        customs179Request.setPayExchangeInfoLists(request.getPayExchangeInfoLists());
        customs179Request.setServiceTime(request.getServiceTime());

        String guid = GUIDUtil.getGuid();
        customs179Request.setSessionID(guid);
        customs179Request.getPayExchangeInfoHead().setGuid(guid);

        String REPORT_TEST_SERVER_URL = Base64.decodeStr(REPORT_TEST_ENV_179_URL_ENCODE);
        String REPORT_PROD_SERVER_URL = Base64.decodeStr(REPORT_PROD_ENV_179_URL_ENCODE);

        String capture179DataUkeyRequest = "\"sessionID\":\"" + customs179Request.getSessionID() + "\"||" +
                "\"payExchangeInfoHead\":\"" + toJson(customs179Request.getPayExchangeInfoHead()) + "\"||" +
                "\"payExchangeInfoLists\":\"" + toJson(customs179Request.getPayExchangeInfoLists()) + "\"||" +
                "\"serviceTime\":" + "\"" + customs179Request.getServiceTime() + "\"";

        @SuppressWarnings("all") UkeyRequest ukeyRequest = new UkeyRequest(METHOD_OF_X509_WITH_HASH, new HashMap<>() {{
            put("inData", capture179DataUkeyRequest.replace("\"", "\\\""));
            put("passwd", "88888888");
        }});
        String signParams = toJson(ukeyRequest);
        SignRequest signRequest = new SignRequest(capture179DataUkeyRequest);
        SignResult signResult = signHandler.sign(signRequest, signParams);

        if (Boolean.FALSE.equals(signResult.getSuccess())) {
            throw new SignException("海关 179 数据抓取加签失败, ukey加签入参: " + signParams + ", 原始入参: " + toJson(customs179Request));
        }
        customs179Request.setCertNo(signResult.getCertNo());
        customs179Request.setSignValue(signResult.getSignatureValue());

        Map<String, Object> formData = new LinkedHashMap<>();
        formData.put("payExInfoStr", toJson(customs179Request));
        log.info("179数据抓取URL: {}\n开始上报，请求数据：{}", REPORT_PROD_SERVER_URL, toJson(formData));

        HttpResponse httpResponse = HttpUtil.createPost(REPORT_PROD_SERVER_URL)
                .form(formData)
                .header("Content-Type", "application/x-www-form-urlencoded", true)
                .timeout(5 * 1000)
                .execute();
        log.info("海关 179 数据抓取返回结果 {}", httpResponse.body());
        Capture179DataResponse capture179DataResponse = new Capture179DataResponse();
        if (httpResponse.isOk()) {
            capture179DataResponse = JSONUtil.toBean(httpResponse.body(), new TypeReference<>() {
            }, true);
        }
        ThirdAbstractResponse<String, Capture179DataResponse> thirdResponse = ThirdAbstractResponse.getInstance();
        thirdResponse.setOriginal(httpResponse.body());
        thirdResponse.setExpected(capture179DataResponse);
        return thirdResponse;
    }

    /**
     * 组装最终请求数据，完成末三段加密
     */
    private MessageRequest buildMessageRequest(AbstractCebMessage cebMessage, IMessageType messageType) {
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
        SignResult signResult;
        Map<String, Object> _args = new HashMap<>();
        String xml = SignHandler.getInitData(new SignRequest(xmlDataInfo));
        _args.put("inData", xml);
        _args.put("passwd", ukeyProperties.getPassword());
        // 签名,返回PEM格式
        Args argsRes1 = signHandler.getUkeyResponseArgs(new UkeyRequest("cus-sec_SpcSignDataAsPEM", _args));
        // 取海关签名证书PEM
        Args argsRes2 = signHandler.getUkeyResponseArgs(new UkeyRequest("cus-sec_SpcGetSignCertAsPEM", new HashMap<>()));

        String payload = signHandler.getDynamicSignDataParameter(ukeyRequest);
        signResult = signHandler.sign(ukeyRequest, payload);
        signResult.setCertNo(argsRes1.getData().get(1));
        signResult.setSignatureValue(argsRes1.getData().get(0));
        signResult.setX509Certificate(argsRes2.getData().get(0));

        if (Boolean.FALSE.equals(signResult.getSuccess())) {
            throw new SignException("CebXxxMessage XML数据加签失败, ukey加签入参: " + payload + ", 原始入参: " + toJson(ukeyRequest));
        }
        String xmlBody = buildXml(signResult, xmlDataInfo, messageType);
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
    private String buildXml(SignResult signResult, String sourceXml, IMessageType messageType) {
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
