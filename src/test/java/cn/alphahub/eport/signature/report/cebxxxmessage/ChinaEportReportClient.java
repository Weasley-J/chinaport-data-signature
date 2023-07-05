package cn.alphahub.eport.signature.report.cebxxxmessage;

import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.report.cebxxxmessage.ChinaEportReportConfiguration.ChinaEportReportProperties;
import cn.alphahub.eport.signature.report.cebxxxmessage.constants.MessageType;
import cn.alphahub.eport.signature.report.cebxxxmessage.entity.BaseTransfer;
import cn.alphahub.eport.signature.report.cebxxxmessage.request.Message;
import cn.alphahub.eport.signature.report.cebxxxmessage.request.MessageBody;
import cn.alphahub.eport.signature.report.cebxxxmessage.request.MessageHead;
import cn.alphahub.eport.signature.report.cebxxxmessage.sgin.CanonicalizationMethod;
import cn.alphahub.eport.signature.report.cebxxxmessage.sgin.DigestMethod;
import cn.alphahub.eport.signature.report.cebxxxmessage.sgin.KeyInfo;
import cn.alphahub.eport.signature.report.cebxxxmessage.sgin.Reference;
import cn.alphahub.eport.signature.report.cebxxxmessage.sgin.Signature;
import cn.alphahub.eport.signature.report.cebxxxmessage.sgin.SignatureMethod;
import cn.alphahub.eport.signature.report.cebxxxmessage.sgin.SignedInfo;
import cn.alphahub.eport.signature.report.cebxxxmessage.sgin.Transforms;
import cn.alphahub.eport.signature.report.cebxxxmessage.sgin.X509Data;
import cn.alphahub.eport.signature.report.cebxxxmessage.util.JAXBUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.alphahub.dtt.plus.util.JacksonUtil.toJson;

/**
 * 电子口岸上报客户端
 */
@Slf4j
@Service
public class ChinaEportReportClient {
    @Autowired
    private SignHandler signHandler;

    @Autowired(required = false)
    private ChinaEportReportProperties chinaEportReportProperties;

    /**
     * 上报海关
     *
     * @param request     原始xml对象
     * @param messageType 311/621/...
     */
    public String push(Object request, MessageType messageType) {
        log.info("海关请求入参{},{}", toJson(request), messageType);
        Map<String, Object> body = sign(request, messageType);
        log.info("海南星创请求入参{}", toJson(body));
        HttpResponse httpResponse = HttpUtil.createPost("http://36.101.208.230:8090/cebcmsg")
                .contentType(ContentType.JSON.getValue())
                .body(toJson(body))
                .execute();
        String responseBody = httpResponse.body();
        log.info("海南星创请求入参 {}, \n原始请求出参: {}", toJson(body), httpResponse.body());
        if (!"OK".equals(responseBody)) {
            log.error("海南星创请求异常，入参 {}, \n异常请求出参: {}", toJson(body), httpResponse.body());
        }
        return httpResponse.body();
    }

    /**
     * 加密，二次组装数据
     */
    public Map<String, Object> sign(Object request, MessageType messageType) {
        if (request == null) {
            return Collections.emptyMap();
        }
        String xmlDataInfo = JAXBUtil.convertToXml(request);
        if (StringUtils.isBlank(xmlDataInfo)) {
            return Collections.emptyMap();
        }

        MessageHead head = new MessageHead();
        head.setMessageType(messageType.getType() + ".xml");
        head.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        xmlDataInfo = xmlDataInfo.replaceAll("xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"", "");

        SignRequest ukeyRequest = new SignRequest();
        ukeyRequest.setId(666);
        ukeyRequest.setData(xmlDataInfo);

        //请求签名加密，二次组装数据 请求加密
        String payload = signHandler.getDynamicSignDataParameter(ukeyRequest);
        SignResult signed = signHandler.sign(ukeyRequest, payload);
        String assembleXml = assembleXml(signed, xmlDataInfo, messageType);

        MessageBody body = MessageBody.builder().data(assembleXml).build();
        Message message = Message.builder().MessageBody(body).MessageHead(head).build();

        Map<String, Object> map = new HashMap<>();
        map.put("Message", message);
        return map;
    }

    /**
     * 组装xml
     */
    private String assembleXml(SignResult signResult, String dataInfo, MessageType messageType) {
        // 加密xml
        Signature signature = Signature.builder().keyInfo(KeyInfo.builder().keyName(signResult.getCertNo()).x509Data(X509Data.builder().X509Certificate(signResult.getX509Certificate()).build()).build())
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
        String signatureStr = JAXBUtil.convertToXml(signature);
        String xml = "</ceb:" + messageType.getType() + ">";
        int index = dataInfo.indexOf(xml);
        StringBuilder builder = new StringBuilder(dataInfo);
        StringBuilder insert = builder.insert(index, signatureStr);
        String asXML = insert.toString();
        if (asXML.startsWith("<?xml ")) {
            asXML = asXML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", "");
        }
        log.info("海南星创，加密报文{}", asXML);
        return Base64.encode(asXML);
    }

    public BaseTransfer assembleBaseTransfer() {
        BaseTransfer baseTransfer = new BaseTransfer();
        baseTransfer.setCopCode(chinaEportReportProperties.getCopCode());
        baseTransfer.setCopName(chinaEportReportProperties.getCopName());
        baseTransfer.setDxpId(chinaEportReportProperties.getDxpId());
        baseTransfer.setDxpMode("DXP");
        return baseTransfer;
    }

}
