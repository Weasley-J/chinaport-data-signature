package cn.alphahub.eport.signature.report.cebxxxmessage;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static cn.alphahub.dtt.plus.util.JacksonUtil.readValue;
import static cn.alphahub.dtt.plus.util.JacksonUtil.toJson;

/**
 * 电子口岸上报客户端
 */
@Slf4j
@Service
public class ChinaEportReportClient {


    @Autowired(required = false)
    private ChinaEportReportProperties chinaEportReportProperties;

    public String push(Object request, MessageType messageType) {
        log.info("海关请求入参{},{}", toJson(request), messageType);
        Map<String, Object> body = sign(request, messageType);
        log.info("海南星创请求入参{}", toJson(body));
        HttpResponse execute = HttpUtil.createPost(chinaEportReportProperties.getHainanXCUrl()).contentType(ContentType.JSON.getValue())
                .body(toJson(body)).execute();
        String body1 = execute.body();
        log.info("海南星创请求入参{},请求出参{}", toJson(body), execute);
        if (!"OK".equals(body1)) {
            log.error("海南星创请求异常，入参{},请求出参{}", toJson(body), execute);
        }
        return execute.body();
    }

    /**
     * 加密，二次组装数据
     */
    public Map<String, Object> sign(Object request, MessageType messageType){
        String dataInfo = JAXBUtil.convertToXml(request, "UTF-8");
        MessageHead head = new MessageHead();
        head.setMessageType(messageType.getType() + ".xml");
        head.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assert dataInfo != null;
        dataInfo = dataInfo.replaceAll("xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"", "");
        //请求签名加密，二次组装数据 请求加密
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("obj", dataInfo);
        log.info("海南加密请求:方法入参{},请求入参{}", toJson(request), toJson(hashMap));
        String string = "";
        SignResult data = readValue(string, SignResult.class);
        String assembleXml = assembleXml(data, dataInfo, messageType);

        MessageBody body = MessageBody.builder().data(assembleXml).build();
        Message message = Message.builder().MessageBody(body).MessageHead(head).build();
        HashMap<String, Object> map = new HashMap<>();
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
