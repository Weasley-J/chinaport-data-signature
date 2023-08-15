package cn.alphahub.eport.signature.core;

import cn.alphahub.eport.signature.base.exception.SignException;
import cn.alphahub.eport.signature.config.ChinaEportProperties;
import cn.alphahub.eport.signature.config.Customs179Properties;
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
import io.github.weasleyj.china.eport.sign.AbstractCebMessage;
import io.github.weasleyj.china.eport.sign.IMessageType;
import io.github.weasleyj.china.eport.sign.model.cebmessage.BaseTransfer;
import io.github.weasleyj.china.eport.sign.model.cebmessage.CEB311Message;
import io.github.weasleyj.china.eport.sign.model.cebmessage.CEB621Message;
import io.github.weasleyj.china.eport.sign.model.cebmessage.export.CEB303Message;
import io.github.weasleyj.china.eport.sign.model.customs179.Customs179Request;
import io.github.weasleyj.china.eport.sign.model.request.Message;
import io.github.weasleyj.china.eport.sign.model.request.MessageBody;
import io.github.weasleyj.china.eport.sign.model.request.MessageHead;
import io.github.weasleyj.china.eport.sign.model.request.MessageRequest;
import io.github.weasleyj.china.eport.sign.model.signature.CanonicalizationMethod;
import io.github.weasleyj.china.eport.sign.model.signature.DigestMethod;
import io.github.weasleyj.china.eport.sign.model.signature.KeyInfo;
import io.github.weasleyj.china.eport.sign.model.signature.Reference;
import io.github.weasleyj.china.eport.sign.model.signature.Signature;
import io.github.weasleyj.china.eport.sign.model.signature.SignatureMethod;
import io.github.weasleyj.china.eport.sign.model.signature.SignedInfo;
import io.github.weasleyj.china.eport.sign.model.signature.Transforms;
import io.github.weasleyj.china.eport.sign.model.signature.X509Data;
import io.github.weasleyj.china.eport.sign.util.GUIDUtil;
import io.github.weasleyj.china.eport.sign.util.JAXBUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static cn.alphahub.dtt.plus.util.JacksonUtil.readValue;
import static cn.alphahub.dtt.plus.util.JacksonUtil.toJson;
import static cn.alphahub.eport.signature.core.CertificateHandler.SING_DATA_METHOD;

/**
 * 电子口岸上报客户端
 *
 * @author weasley
 * @since 1.1.0
 */
@Slf4j
@Component
@AllArgsConstructor
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
    private final SignHandler signHandler;
    private final UkeyProperties ukeyProperties;
    private final Customs179Properties customs179Properties;
    private final ChinaEportProperties chinaEportProperties;

    /**
     * 构建 ds:Signature 节点
     *
     * @param signResult 加签结果
     * @return ds:Signature 节点
     */
    public static String buildSignatureNode(SignResult signResult) {
        KeyInfo keyInfo = KeyInfo.builder()
                .keyName(signResult.getCertNo())
                .x509Data(new X509Data(signResult.getX509Certificate()))
                .build();

        Reference reference = Reference.builder()
                .digestMethod(new DigestMethod())
                .uri("")
                .transforms(new Transforms())
                .digestValue(signResult.getDigestValue())
                .build();

        SignedInfo signedInfo = SignedInfo.builder()
                .CanonicalizationMethod(new CanonicalizationMethod())
                .SignatureMethod(new SignatureMethod().setAlgorithm(SignatureHandler.getSignatureMethodAlgorithm()))
                .reference(reference)
                .build();

        Signature signature = Signature.builder()
                .keyInfo(keyInfo)
                .signatureValue(signResult.getSignatureValue())
                .signedInfo(signedInfo)
                .build();

        return JAXBUtil.toXml(signature);
    }

    /**
     * 构建 ds:SignedInfo 节点
     *
     * @param signResult 加签结果
     * @return ds:SignedInfo 节点
     */
    public static String buildSignedInfoNode(SignResult signResult) {
        Reference reference = Reference.builder()
                .digestMethod(new DigestMethod())
                .uri("")
                .transforms(new Transforms())
                .digestValue(signResult.getDigestValue())
                .build();
        SignedInfo signedInfo = SignedInfo.builder()
                .CanonicalizationMethod(new CanonicalizationMethod())
                .SignatureMethod(new SignatureMethod().setAlgorithm(SignatureHandler.getSignatureMethodAlgorithm()))
                .reference(reference)
                .build();
        return JAXBUtil.toXml(signedInfo);
    }

    /**
     * CebXxxMessage数据上报海关
     *
     * @param cebMessage  原始xml对象
     * @param messageType CEB311Message|CEB621Message|...
     * @return 推送结果, OK: 表示成功, 报关结果需要自己查询回执
     */
    public ThirdAbstractResponse<MessageRequest, String, String> report(AbstractCebMessage cebMessage, IMessageType messageType) {
        log.info("数据上报海关xml入参: {}, {}", JSONUtil.toJsonStr(cebMessage), messageType.getType());
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
            log.error("上报海关请求异常, 请求入参: {}\n海关Http响应: {}", requestBody, responseBody);
        }
        ThirdAbstractResponse<MessageRequest, String, String> thirdResponse = ThirdAbstractResponse.getInstance();
        thirdResponse.setPayload(messageRequest);
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
    public ThirdAbstractResponse<Map<String, Object>, String, Capture179DataResponse> capture179Data(Capture179DataRequest request) {
        log.info("海关 179 数据抓取 {}", JSONUtil.toJsonStr(request));
        Customs179Request customs179Request = new Customs179Request();
        customs179Request.setSessionID(request.getSessionID());
        customs179Request.setPayExchangeInfoHead(request.getPayExchangeInfoHead());
        customs179Request.setPayExchangeInfoLists(request.getPayExchangeInfoLists());
        customs179Request.setServiceTime(request.getServiceTime());

        String guid = GUIDUtil.getGuid();
        customs179Request.setSessionID(guid);
        customs179Request.getPayExchangeInfoHead().setGuid(guid);
        customs179Request.getPayExchangeInfoHead().setEbpCode(customs179Properties.getEbpCode());

        String testEnvServerUrl = Base64.decodeStr(REPORT_TEST_ENV_179_URL_ENCODE);
        String prodEnvServerUrl = Base64.decodeStr(REPORT_PROD_ENV_179_URL_ENCODE);
        String requestUrl = StringUtils.defaultIfBlank(customs179Properties.getServer(), prodEnvServerUrl);

        String capture179DataUkeyRequest = "\"sessionID\":\"" + customs179Request.getSessionID() + "\"||" +
                "\"payExchangeInfoHead\":\"" + toJson(customs179Request.getPayExchangeInfoHead()) + "\"||" +
                "\"payExchangeInfoLists\":\"" + toJson(customs179Request.getPayExchangeInfoLists()) + "\"||" +
                "\"serviceTime\":" + "\"" + customs179Request.getServiceTime() + "\"";

        @SuppressWarnings("all") UkeyRequest ukeyRequest = new UkeyRequest(SING_DATA_METHOD, new HashMap<>() {{
            put("inData", capture179DataUkeyRequest);
            put("passwd", ukeyProperties.getPassword());
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
        log.info("海关179数据抓取URL: {}\n开始上报，请求数据：{}", requestUrl, toJson(formData));

        HttpResponse httpResponse = HttpUtil.createPost(requestUrl)
                .form(formData)
                .header("Content-Type", "application/x-www-form-urlencoded", true)
                .timeout(5 * 1000)
                .execute();
        log.info("海关 179 数据抓取返回结果 {}", httpResponse.body());
        Capture179DataResponse expected = readValue("""
                {"code":"10000","message":"上传成功","total":0,"serviceTime":null}
                """, Capture179DataResponse.class);
        expected.setServiceTime(Long.parseLong(customs179Request.getServiceTime()));
        ThirdAbstractResponse<Map<String, Object>, String, Capture179DataResponse> thirdResponse = ThirdAbstractResponse.getInstance();
        thirdResponse.setPayload(formData);
        thirdResponse.setOriginal(httpResponse.body());
        thirdResponse.setExpected(expected);
        return thirdResponse;
    }

    /**
     * 推断CebMessage的具体的XML入参解析成Java对象
     *
     * @implNote 已实现: CEB311Message,CEB621Message,CEB303Message
     */
    @SuppressWarnings("all")
    public AbstractCebMessage buildCebMessage(UploadCEBMessageRequest request) {
        String guid = GUIDUtil.getGuid();
        String cebMessage = String.valueOf(request.getCebMessage());
        switch (request.getDataType()) {
            case XML -> {
                return switch (request.getMessageType()) {
                    // 进口单XML报文解析
                    case CEB311Message -> {
                        CEB311Message ceb311Message = Objects.requireNonNull(JAXBUtil.toBean(cebMessage, CEB311Message.class));
                        ceb311Message.setGuid(guid);
                        ceb311Message.getOrder().getOrderHead().setGuid(guid);
                        buildBaseTransfer(ceb311Message.getBaseTransfer());
                        yield ceb311Message;
                    }
                    case CEB312Message -> null;
                    case CEB411Message -> null;
                    case CEB412Message -> null;
                    case CEB511Message -> null;
                    case CEB512Message -> null;
                    case CEB513Message -> null;
                    case CEB514Message -> null;
                    case CEB621Message -> {
                        CEB621Message ceb621Message = Objects.requireNonNull(JAXBUtil.toBean(cebMessage, CEB621Message.class));
                        ceb621Message.setGuid(guid);
                        ceb621Message.getInventory().getInventoryHead().setGuid(guid);
                        buildBaseTransfer(ceb621Message.getBaseTransfer());
                        yield ceb621Message;
                    }
                    case CEB622Message -> null;
                    case CEB623Message -> null;
                    case CEB624Message -> null;
                    case CEB625Message -> null;
                    case CEB626Message -> null;
                    case CEB711Message -> null;
                    case CEB712Message -> null;
                    case CEB213Message -> null;
                    case CEB214Message -> null;
                    case CEB215Message -> null;
                    case CEB216Message -> null;
                    case CEB303Message -> {
                        CEB303Message ceb303Message = Objects.requireNonNull(JAXBUtil.toBean(cebMessage, CEB303Message.class));
                        ceb303Message.setGuid(guid);
                        ceb303Message.getOrder().getOrderHead().setGuid(guid);
                        buildBaseTransfer(ceb303Message.getBaseTransfer());
                        yield ceb303Message;
                    }
                    // 出口单XML报文
                    case CEB304Message -> null;
                    case CEB403Message -> null;
                    case CEB404Message -> null;
                    case CEB505Message -> null;
                    case CEB506Message -> null;
                    case CEB507Message -> null;
                    case CEB508Message -> null;
                    case CEB509Message -> null;
                    case CEB510Message -> null;
                    case CEB603Message -> null;
                    case CEB604Message -> null;
                    case CEB605Message -> null;
                    case CEB606Message -> null;
                    case CEB607Message -> null;
                    case CEB608Message -> null;
                    case CEB701Message -> null;
                    case CEB702Message -> null;
                    case CEB792Message -> null;
                };
            }
            case JSON -> {
                return switch (request.getMessageType()) {
                    case CEB311Message -> {
                        CEB311Message ceb311Message = JSONUtil.toBean(cebMessage, new TypeReference<>() {
                        }, false);
                        ceb311Message.setGuid(guid);
                        ceb311Message.getOrder().getOrderHead().setGuid(guid);
                        buildBaseTransfer(ceb311Message.getBaseTransfer());
                        yield ceb311Message;
                    }
                    // 进口单JSON报文解析
                    case CEB312Message -> null;
                    case CEB411Message -> null;
                    case CEB412Message -> null;
                    case CEB511Message -> null;
                    case CEB512Message -> null;
                    case CEB513Message -> null;
                    case CEB514Message -> null;
                    case CEB621Message -> {
                        CEB621Message ceb621Message = JSONUtil.toBean(cebMessage, new TypeReference<>() {
                        }, false);
                        ceb621Message.setGuid(guid);
                        ceb621Message.getInventory().getInventoryHead().setGuid(guid);
                        buildBaseTransfer(ceb621Message.getBaseTransfer());
                        yield ceb621Message;
                    }
                    case CEB622Message -> null;
                    case CEB623Message -> null;
                    case CEB624Message -> null;
                    case CEB625Message -> null;
                    case CEB626Message -> null;
                    case CEB711Message -> null;
                    case CEB712Message -> null;
                    case CEB213Message -> null;
                    case CEB214Message -> null;
                    case CEB215Message -> null;
                    case CEB216Message -> null;
                    // 出口单JSON报文解析
                    case CEB303Message -> {
                        CEB303Message ceb303Message = JSONUtil.toBean(cebMessage, new TypeReference<>() {
                        }, false);
                        ceb303Message.setGuid(guid);
                        ceb303Message.getOrder().getOrderHead().setGuid(guid);
                        buildBaseTransfer(ceb303Message.getBaseTransfer());
                        yield ceb303Message;
                    }
                    case CEB304Message -> null;
                    case CEB403Message -> null;
                    case CEB404Message -> null;
                    case CEB505Message -> null;
                    case CEB506Message -> null;
                    case CEB507Message -> null;
                    case CEB508Message -> null;
                    case CEB509Message -> null;
                    case CEB510Message -> null;
                    case CEB603Message -> null;
                    case CEB604Message -> null;
                    case CEB605Message -> null;
                    case CEB606Message -> null;
                    case CEB607Message -> null;
                    case CEB608Message -> null;
                    case CEB701Message -> null;
                    case CEB702Message -> null;
                    case CEB792Message -> null;
                };
            }
            default -> throw new IllegalStateException("Unexpected value: " + toJson(request));
        }
    }

    /**
     * Build BaseTransfer XML Node
     */
    @SuppressWarnings("all")
    public BaseTransfer buildBaseTransfer(BaseTransfer baseTransfer) {
        baseTransfer.setCopCode(StringUtils.defaultIfBlank(baseTransfer.getCopCode(), chinaEportProperties.getCopCode()));
        baseTransfer.setCopName(StringUtils.defaultIfBlank(baseTransfer.getCopName(), chinaEportProperties.getCopName()));
        baseTransfer.setDxpId(StringUtils.defaultIfBlank(baseTransfer.getDxpId(), chinaEportProperties.getDxpId()));
        baseTransfer.setDxpMode(StringUtils.defaultIfBlank(baseTransfer.getDxpMode(), "DXP"));
        return baseTransfer;
    }

    /**
     * 组装最终请求数据，完成末三段加密
     */
    public MessageRequest buildMessageRequest(AbstractCebMessage cebMessage, IMessageType messageType) {
        if (cebMessage == null) {
            return new MessageRequest();
        }
        String sourceXml = JAXBUtil.toXml(cebMessage);
        MessageHead messageHead = new MessageHead();
        messageHead.setMessageType(messageType.getType() + ".xml");
        messageHead.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        sourceXml = sourceXml.replaceAll("xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"", "");

        SignRequest ukeyRequest = new SignRequest();
        ukeyRequest.setId(1);
        ukeyRequest.setData(sourceXml);

        // 请求签名加密，2次组装数据, 请求加密（包含第1，2段加密）
        String payload = signHandler.getSignDataParameter(ukeyRequest);
        SignResult signResult = signHandler.sign(ukeyRequest, payload);
        // 线程uppark时间可能会失败，增强方法兜底
        if (Boolean.FALSE.equals(signResult.getSuccess())) {
            this.enhanceSignResultIfDefective(signResult, sourceXml);
        }
        if (Boolean.FALSE.equals(signResult.getSuccess())) {
            throw new SignException("CebXxxMessage XML数据加签失败, ukey加签入参: " + payload + ", 原始入参: " + toJson(ukeyRequest));
        }
        String xmlBody = buildXml(signResult, sourceXml, messageType);
        // 第3次组装数据, 将加签后的 XM 进行 base64 加密
        String encodedXml = Base64.encode(xmlBody);
        MessageBody messageBody = MessageBody.builder().data(encodedXml).build();
        Message message = Message.builder().MessageBody(messageBody).MessageHead(messageHead).build();
        return new MessageRequest(message);
    }

    /**
     * 增强 SignResult, 如果有缺陷: 如: 验签失败等场景
     *
     * @param signResult 签名结果
     * @param sourceXml  原始XML
     * @return 签名结果
     * @apiNote 用于 SignHandler#sign 结果出现验签失败时的兜底手段
     */
    @SuppressWarnings("all")
    private SignResult enhanceSignResultIfDefective(SignResult signResult, String sourceXml) {
        String signatureXml = SignHandler.getInitData(new SignRequest(sourceXml));
        Map<String, Object> params = new HashMap<>();
        params.put("inData", signatureXml);
        params.put("passwd", ukeyProperties.getPassword());
        Args args1 = signHandler.getUkeyResponseArgs(new UkeyRequest("cus-sec_SpcSignDataAsPEM", params));
        Args args2 = signHandler.getUkeyResponseArgs(new UkeyRequest("cus-sec_SpcGetSignCertAsPEM", new HashMap<>()));
        signResult.setCertNo(args1.getData().get(1));
        signResult.setSignatureValue(args1.getData().get(0));
        signResult.setX509Certificate(args2.getData().get(0));
        if (signResult.getSuccess().equals(false)) {
            signResult.setSuccess(true);
        }
        return signResult;
    }

    /**
     * 组装Signature XML节点
     *
     * @param sourceXml 原始XML
     */
    private String buildXml(SignResult signResult, String sourceXml, IMessageType messageType) {
        String signatureNode = buildSignatureNode(signResult);
        String xmlStart = "</ceb:" + messageType.getType() + ">";
        int index = sourceXml.indexOf(xmlStart);
        StringBuilder builder = new StringBuilder(sourceXml);
        builder.insert(index, signatureNode.concat("\n"));
        String finalXml = builder.toString();
        if (finalXml.startsWith("<?xml ")) {
            finalXml = finalXml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", "");
        }
        log.info("上报海关的XML报文:\n{}", finalXml);
        return finalXml;
    }

}
