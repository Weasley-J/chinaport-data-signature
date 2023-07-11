package cn.alphahub.eport.signature.controller.rpc;

import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.alphahub.eport.signature.entity.UkeyResponse.Args;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.Map;

import static cn.alphahub.eport.signature.core.SignatureHandler.getSignatureNodeBeforeSend;

/**
 * Eport Sign Controller Test
 */
@SpringBootTest
class EportSignControllerTest {
    @Autowired
    SignHandler signHandler;
    @Autowired
    EportSignController controller;

    @Test
    @DisplayName("海关数据加签+手动测试验签结果")
    void signAndVerifySignature() {
        String data = """
                {
                  "id": 1,
                  "data": "<?xml version=\\"1.0\\" encoding=\\"UTF-8\\"?>\\n<ceb:CEB311Message guid=\\"tCEB311_HNZB_HNFX_20230707223752_003\\" version=\\"1.0\\" xmlns:ceb=\\"http://www.chinaport.gov.cn/ceb\\" xmlns:xsi=\\"http://www.w3.org/2001/XMLSchema-instance\\">\\n    <ceb:Order>\\n        <ceb:OrderHead>\\n            <ceb:guid>tCEB311_HNZB_HNFX_20230707223752_001</ceb:guid>\\n            <ceb:appType>1</ceb:appType>\\n            <ceb:appTime>20230704181028</ceb:appTime>\\n            <ceb:appStatus>2</ceb:appStatus>\\n            <ceb:orderType>I</ceb:orderType>\\n            <ceb:orderNo>T_C5051511332138160010</ceb:orderNo>\\n            <ceb:ebpCode>4601630004</ceb:ebpCode>\\n            <ceb:ebpName>海南省荣誉进出口贸易有限公司</ceb:ebpName>\\n            <ceb:ebcCode>4601630004</ceb:ebcCode>\\n            <ceb:ebcName>海南省荣誉进出口贸易有限公司</ceb:ebcName>\\n            <ceb:goodsValue>0.01</ceb:goodsValue>\\n            <ceb:freight>0</ceb:freight>\\n            <ceb:discount>0</ceb:discount>\\n            <ceb:taxTotal>0</ceb:taxTotal>\\n            <ceb:acturalPaid>0.01</ceb:acturalPaid>\\n            <ceb:currency>142</ceb:currency>\\n            <ceb:buyerRegNo>4</ceb:buyerRegNo>\\n            <ceb:buyerName>袁晓雨</ceb:buyerName>\\n            <ceb:buyerTelephone>13701727375</ceb:buyerTelephone>\\n            <ceb:buyerIdType>1</ceb:buyerIdType>\\n            <ceb:buyerIdNumber>130435200009241538</ceb:buyerIdNumber>\\n            <ceb:consignee>袁晓雨</ceb:consignee>\\n            <ceb:consigneeTelephone>13701727375</ceb:consigneeTelephone>\\n            <ceb:consigneeAddress>北京北京市东城区</ceb:consigneeAddress>\\n            <ceb:note>test</ceb:note>\\n        </ceb:OrderHead>\\n        <ceb:OrderList>\\n            <ceb:gnum>1</ceb:gnum>\\n            <ceb:itemNo>1</ceb:itemNo>\\n            <ceb:itemName>LANNA兰纳</ceb:itemName>\\n            <ceb:gmodel>10片/包</ceb:gmodel>\\n            <ceb:itemDescribe></ceb:itemDescribe>\\n            <ceb:barCode>1</ceb:barCode>\\n            <ceb:unit>011</ceb:unit>\\n            <ceb:qty>1</ceb:qty>\\n            <ceb:price>1</ceb:price>\\n            <ceb:totalPrice>1</ceb:totalPrice>\\n            <ceb:currency>142</ceb:currency>\\n            <ceb:country>136</ceb:country>\\n            <ceb:note>test</ceb:note>\\n        </ceb:OrderList>\\n    </ceb:Order>\\n    <ceb:BaseTransfer>\\n        <ceb:copCode>4601630004</ceb:copCode>\\n        <ceb:copName>海南省荣誉进出口贸易有限公司</ceb:copName>\\n        <ceb:dxpMode>DXP</ceb:dxpMode>\\n        <ceb:dxpId>DXPENT0000530815</ceb:dxpId>\\n        <ceb:note>test</ceb:note>\\n    </ceb:BaseTransfer>\\n</ceb:CEB311Message>"
                }
                """;
        SignRequest request = JSONUtil.toBean(data, SignRequest.class);
        SignResult result = controller.signature(request).getData();
        Map<String, Object> argsMap = new LinkedHashMap<>();
        argsMap.put("inData", getSignatureNodeBeforeSend(request));
        argsMap.put("signData", result.getSignatureValue());
        argsMap.put("certDataPEM", result.getX509Certificate());
        Args args = signHandler.getUkeyResponseArgs(new UkeyRequest("cus-sec_SpcVerifySignData", argsMap));
        System.err.println("验签结果: " + args.getData().get(0));
    }
}
