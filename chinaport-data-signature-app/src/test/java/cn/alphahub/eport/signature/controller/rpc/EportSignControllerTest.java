package cn.alphahub.eport.signature.controller.rpc;

import cn.alphahub.eport.signature.config.ChinaEportProperties;
import cn.alphahub.eport.signature.core.ChinaEportReportClient;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.alphahub.eport.signature.entity.UkeyResponse.Args;
import cn.hutool.core.codec.Base64;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import o.github.weasleyj.china.eport.sign.constants.MessageType;
import o.github.weasleyj.china.eport.sign.model.cebmessage.CEB311Message;
import o.github.weasleyj.china.eport.sign.model.request.MessageRequest;
import o.github.weasleyj.china.eport.sign.util.GUIDUtil;
import o.github.weasleyj.china.eport.sign.util.JAXBUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.alphahub.eport.signature.core.ChinaEportReportClient.EPORT_CEBMESSAGE_SERVER_ENCODE;

/**
 * Eport Sign Controller Test
 */
@Slf4j
@SpringBootTest
class EportSignControllerTest {
    @Autowired
    SignHandler signHandler;
    @Autowired
    EportSignController controller;
    @Autowired
    ChinaEportProperties chinaEportProperties;
    @Autowired
    ChinaEportReportClient chinaEportReportClient;

    @Test
    @DisplayName("海关XML数据加签+验正签名结果")
    void signAndVerifyXMLSignature() {
        String data = """
                {
                  "id": 1,
                  "data": "<?xml version=\\"1.0\\" encoding=\\"UTF-8\\"?>\\n<ceb:CEB311Message guid=\\"tCEB311_HNZB_HNFX_20230707223752_003\\" version=\\"1.0\\" xmlns:ceb=\\"http://www.chinaport.gov.cn/ceb\\" xmlns:xsi=\\"http://www.w3.org/2001/XMLSchema-instance\\">\\n    <ceb:Order>\\n        <ceb:OrderHead>\\n            <ceb:guid>tCEB311_HNZB_HNFX_20230707223752_001</ceb:guid>\\n            <ceb:appType>1</ceb:appType>\\n            <ceb:appTime>20230704181028</ceb:appTime>\\n            <ceb:appStatus>2</ceb:appStatus>\\n            <ceb:orderType>I</ceb:orderType>\\n            <ceb:orderNo>T_C5051511332138160010</ceb:orderNo>\\n            <ceb:ebpCode>4601630004</ceb:ebpCode>\\n            <ceb:ebpName>海南省荣誉进出口贸易有限公司</ceb:ebpName>\\n            <ceb:ebcCode>4601630004</ceb:ebcCode>\\n            <ceb:ebcName>海南省荣誉进出口贸易有限公司</ceb:ebcName>\\n            <ceb:goodsValue>0.01</ceb:goodsValue>\\n            <ceb:freight>0</ceb:freight>\\n            <ceb:discount>0</ceb:discount>\\n            <ceb:taxTotal>0</ceb:taxTotal>\\n            <ceb:acturalPaid>0.01</ceb:acturalPaid>\\n            <ceb:currency>142</ceb:currency>\\n            <ceb:buyerRegNo>4</ceb:buyerRegNo>\\n            <ceb:buyerName>袁晓雨</ceb:buyerName>\\n            <ceb:buyerTelephone>13701727375</ceb:buyerTelephone>\\n            <ceb:buyerIdType>1</ceb:buyerIdType>\\n            <ceb:buyerIdNumber>130435200009241538</ceb:buyerIdNumber>\\n            <ceb:consignee>袁晓雨</ceb:consignee>\\n            <ceb:consigneeTelephone>13701727375</ceb:consigneeTelephone>\\n            <ceb:consigneeAddress>北京北京市东城区</ceb:consigneeAddress>\\n            <ceb:note>test</ceb:note>\\n        </ceb:OrderHead>\\n        <ceb:OrderList>\\n            <ceb:gnum>1</ceb:gnum>\\n            <ceb:itemNo>1</ceb:itemNo>\\n            <ceb:itemName>LANNA兰纳</ceb:itemName>\\n            <ceb:gmodel>10片/包</ceb:gmodel>\\n            <ceb:itemDescribe></ceb:itemDescribe>\\n            <ceb:barCode>1</ceb:barCode>\\n            <ceb:unit>011</ceb:unit>\\n            <ceb:qty>1</ceb:qty>\\n            <ceb:price>1</ceb:price>\\n            <ceb:totalPrice>1</ceb:totalPrice>\\n            <ceb:currency>142</ceb:currency>\\n            <ceb:country>136</ceb:country>\\n            <ceb:note>test</ceb:note>\\n        </ceb:OrderList>\\n    </ceb:Order>\\n    <ceb:BaseTransfer>\\n        <ceb:copCode>4601630004</ceb:copCode>\\n        <ceb:copName>海南省荣誉进出口贸易有限公司</ceb:copName>\\n        <ceb:dxpMode>DXP</ceb:dxpMode>\\n        <ceb:dxpId>DXPENT0000530815</ceb:dxpId>\\n        <ceb:note>test</ceb:note>\\n    </ceb:BaseTransfer>\\n</ceb:CEB311Message>"
                }
                """;
        SignRequest request = JSONUtil.toBean(data, SignRequest.class);
        SignResult result = controller.signature(request).getData();
        Map<String, Object> argsMap = new LinkedHashMap<>();
        argsMap.put("inData", result.getSignatureNode());
        argsMap.put("signData", result.getSignatureValue());
        argsMap.put("certDataPEM", result.getX509Certificate());
        Args args = signHandler.getUkeyResponseArgs(new UkeyRequest("cus-sec_SpcVerifySignData", argsMap));
        System.err.println("验签结果: " + args.getData().get(0));
    }

    @Test
    @DisplayName("海关179数据加签+验正签名结果")
    void signAndVerify179Signature() {
        String data = """
                {
                  "id": 1,
                  "data": "\\"sessionID\\":\\"DUYOK9-WEASLEY-20230715114316-0UTMKF\\"||\\"payExchangeInfoHead\\":\\"{\\"guid\\":\\"DUYOK9-WEASLEY-20230715114316-0UTMKF\\",\\"initalRequest\\":\\"<xml><appid><![CDATA[xwxwxwxwx112121]]></appid><mch_id>11111111</mch_id><body><![CDATA[测试测试]]></body><nonce_str><![CDATA[ashdgahgdhaghgliqwueyqu1]]></nonce_str><out_trade_no>202009231454212140210352</out_trade_no><total_fee>500</total_fee><spbill_create_ip><![CDATA[192.168.0.1]]></spbill_create_ip><notify_url><![CDATA[https://www.baidu.com]]></notify_url><openid><![CDATA[adasyduiyasdhjxzycua-Q]]></openid><trade_type><![CDATA[JSAPI]]></trade_type><sign><![CDATA[asd1sa56d4545wqe44wq5]]></sign></xml>\\",\\"initalResponse\\":\\"<xml><appid><![CDATA[xwxwxwxwx112121]]></appid><bank_type><![CDATA[OTHERS]]></bank_type><cash_fee>500</cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id>11111111</mch_id><nonce_str><![CDATA[ashdgahgdhaghgliqwueyqu1]]></nonce_str><openid><![CDATA[adasyduiyasdhjxzycua-Q]]></openid><out_trade_no>202009231454212140210352</out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[asgdhasgdhasgdhgasgdhasgdh]]></sign><time_end>20200923145426</time_end><total_fee>500</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id>4200000681202009235085032319</transaction_id></xml>\\",\\"ebpCode\\":\\"46016602EV\\",\\"payCode\\":\\"1537923071\\",\\"payTransactionId\\":\\"4200000681202009235085032319\\",\\"totalAmount\\":5.0,\\"currency\\":\\"142\\",\\"verDept\\":\\"3\\",\\"payType\\":\\"4\\",\\"tradingTime\\":\\"20200923145426\\",\\"note\\":\\"\\"}\\"||\\"payExchangeInfoLists\\":\\"[{\\"orderNo\\":\\"202009231454218421271832\\",\\"goodsInfo\\":[{\\"gname\\":\\"济州花梨精华面膜\\",\\"itemLink\\":\\"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999761&shopId=453\\"}],\\"recpAccount\\":\\"46016602EV\\",\\"recpCode\\":\\"46016602EV\\",\\"recpName\\":\\"海南省荣誉进出口贸易有限公司\\"}]\\"||\\"serviceTime\\":\\"1689392596517\\"";
                }
                """;
        SignRequest request = JSONUtil.toBean(data, SignRequest.class);
        SignResult result = controller.signature(request).getData();
        Map<String, Object> argsMap = new LinkedHashMap<>();
        argsMap.put("inData", request.getData());
        argsMap.put("signData", result.getSignatureValue());
        argsMap.put("certDataPEM", result.getX509Certificate());
        Args args = signHandler.getUkeyResponseArgs(new UkeyRequest("cus-sec_SpcVerifySignData", argsMap));
        System.err.println("验签结果: " + args.getData().get(0));
    }

    @Test
    @DisplayName("模拟海关XML数据加签推单")
    void signAndVerifyXMLSignatureAndPush() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <ceb:CEB311Message guid="CEB311_HNZB_HNFX_20230707223752_010" version="1.0" xmlns:ceb="http://www.chinaport.gov.cn/ceb" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                    <ceb:Order>
                        <ceb:OrderHead>
                            <ceb:guid>CEB311_HNZB_HNFX_20230707223752_010</ceb:guid>
                            <ceb:appType>1</ceb:appType>
                            <ceb:appTime>20230704181028</ceb:appTime>
                            <ceb:appStatus>2</ceb:appStatus>
                            <ceb:orderType>I</ceb:orderType>
                            <ceb:orderNo>T_C5051511332138160010</ceb:orderNo>
                            <ceb:ebpCode>4601630004</ceb:ebpCode>
                            <ceb:ebpName>海南省荣誉进出口贸易有限公司</ceb:ebpName>
                            <ceb:ebcCode>4601630004</ceb:ebcCode>
                            <ceb:ebcName>海南省荣誉进出口贸易有限公司</ceb:ebcName>
                            <ceb:goodsValue>0.01</ceb:goodsValue>
                            <ceb:freight>0</ceb:freight>
                            <ceb:discount>0</ceb:discount>
                            <ceb:taxTotal>0</ceb:taxTotal>
                            <ceb:acturalPaid>0.01</ceb:acturalPaid>
                            <ceb:currency>142</ceb:currency>
                            <ceb:buyerRegNo>4</ceb:buyerRegNo>
                            <ceb:buyerName>袁晓雨</ceb:buyerName>
                            <ceb:buyerTelephone>13701727375</ceb:buyerTelephone>
                            <ceb:buyerIdType>1</ceb:buyerIdType>
                            <ceb:buyerIdNumber>130435200009241538</ceb:buyerIdNumber>
                            <ceb:consignee>袁晓雨</ceb:consignee>
                            <ceb:consigneeTelephone>13701727375</ceb:consigneeTelephone>
                            <ceb:consigneeAddress>北京北京市东城区</ceb:consigneeAddress>
                            <ceb:note>test</ceb:note>
                        </ceb:OrderHead>
                        <ceb:OrderList>
                            <ceb:gnum>1</ceb:gnum>
                            <ceb:itemNo>1</ceb:itemNo>
                            <ceb:itemName>LANNA兰纳</ceb:itemName>
                            <ceb:gmodel>10片/包</ceb:gmodel>
                            <ceb:itemDescribe></ceb:itemDescribe>
                            <ceb:barCode>1</ceb:barCode>
                            <ceb:unit>011</ceb:unit>
                            <ceb:qty>1</ceb:qty>
                            <ceb:price>1</ceb:price>
                            <ceb:totalPrice>1</ceb:totalPrice>
                            <ceb:currency>142</ceb:currency>
                            <ceb:country>136</ceb:country>
                            <ceb:note>test</ceb:note>
                        </ceb:OrderList>
                    </ceb:Order>
                    <ceb:BaseTransfer>
                        <ceb:copCode>4601630004</ceb:copCode>
                        <ceb:copName>海南省荣誉进出口贸易有限公司</ceb:copName>
                        <ceb:dxpMode>DXP</ceb:dxpMode>
                        <ceb:dxpId>DXPENT0000530815</ceb:dxpId>
                        <ceb:note>test</ceb:note>
                    </ceb:BaseTransfer>
                </ceb:CEB311Message>
                """;
        SignRequest request = new SignRequest(xml);
        SignResult result = controller.signature(request).getData();

        // 组装数据
        CEB311Message ceb311Message = JAXBUtil.toBean(xml, CEB311Message.class);
        String guid = GUIDUtil.getGuid();
        assert ceb311Message != null;
        ceb311Message.setGuid(guid);
        ceb311Message.getOrder().getOrderHead().setGuid(guid);
        chinaEportReportClient.buildBaseTransfer(ceb311Message.getBaseTransfer());
        MessageRequest messageRequest = chinaEportReportClient.buildMessageRequest(ceb311Message, MessageType.CEB311Message);
        String requestServer = StringUtils.defaultIfBlank(chinaEportProperties.getServer(), Base64.decodeStr(EPORT_CEBMESSAGE_SERVER_ENCODE));
        String requestBody = JSONUtil.toJsonStr(messageRequest);
        log.info("数据上报海关请求入参 {}\nRequest Server: {}", requestBody, requestServer);
        HttpResponse httpResponse = HttpUtil.createPost(requestServer + "/cebcmsg")
                .contentType(ContentType.JSON.getValue())
                .body(requestBody)
                .execute();
        log.info("开始上报，Http响应结果: {}", httpResponse.body());

        // 睡8秒，开始查询回执
        LocalDateTime uploadTime = LocalDateTime.now();
        int sleepTime = 15;
        for (int i = sleepTime; i > 0; i--) {
            log.warn("开始查询推送回执，海关后台正在处理中，线程挂起剩余 {} 秒。", i);
            TimeUnit.SECONDS.sleep(1);
        }
        String url = "http://" + Base64.decodeStr("MzYuMTAxLjIwOC4yMzA=") + ":8090/ceb312msg?" + HttpUtil.toParams(new LinkedHashMap<>() {{
            put("dxpid", chinaEportProperties.getDxpId());
            put("qryid", DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(uploadTime));
        }});
        HttpResponse receiptResponse = HttpUtil.createGet(url).execute();
        log.info("\n{}\n回执结果: {}", url, receiptResponse.body()); //回执结果: [{org_id=1, instm=1690556389000, re_order_guid=AOF0YM-WEASLEY-20230728225932-6QVICD, returnstatus=-301014, ebpcode=4601630004, returntime2=20230728225949, ebccode=4601630004, trans_dxpid=DXPENT0000530815, returninfo=订单报文处理失败，接收报文为旧报文数据，不做处理;, returntime=1690556377000, orderno=T_C5051511332138160010}]
    }
}
