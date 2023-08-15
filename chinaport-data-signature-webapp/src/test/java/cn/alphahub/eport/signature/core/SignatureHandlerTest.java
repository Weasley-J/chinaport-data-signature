package cn.alphahub.eport.signature.core;

import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

/**
 * 核心方法测试
 */
@Slf4j
@SpringBootTest
class SignatureHandlerTest {

    /**
     * CEB621Message
     */
    public static String CEB621Message;

    static {
        try {
            File CEB621MessageFile = ResourceUtils.getFile("classpath:xml/CEB621Message.xml");
            CEB621Message = IoUtil.read(IoUtil.toStream(CEB621MessageFile), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Resource
    private SignHandler signHandler;

    @Test
    @DisplayName("获取CEBXxxMessage的签名值")
    void getSignatureValue() {
        SignRequest request = new SignRequest(sourceXml());
        String payload = signHandler.getSignDataParameter(request);
        String jsonPrettyStr = JSONUtil.toJsonPrettyStr(payload);
        SignResult sign = signHandler.sign(request, jsonPrettyStr);
        System.err.println("CEBMessage的签名结果: \n" + JSONUtil.toJsonPrettyStr(sign));
        System.err.println("\nSignatureValue的值: " + sign.getSignatureValue());
    }

    private String getPayload() {
        return """
                {
                    "_method": "cus-sec_SpcSignDataAsPEM",
                    "_id": 1,
                    "args": {
                        "inData": "<ds:SignedInfo xmlns:ceb=\\"http://www.chinaport.gov.cn/ceb\\" xmlns:ds=\\"http://www.w3.org/2000/09/xmldsig#\\" xmlns:xsi=\\"http://www.w3.org/2001/XMLSchema-instance\\"><ds:CanonicalizationMethod Algorithm=\\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\\"></ds:CanonicalizationMethod><ds:SignatureMethod Algorithm=\\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\\"></ds:SignatureMethod><ds:Reference URI=\\"\\"><ds:Transforms><ds:Transform Algorithm=\\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\\"></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm=\\"http://www.w3.org/2000/09/xmldsig#sha1\\"></ds:DigestMethod><ds:DigestValue>yuKZwi7DCnriGmRcQS3/fqwgyuA=</ds:DigestValue></ds:Reference></ds:SignedInfo>",
                        "passwd": "88888888"
                    }
                }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   }
                """;
    }

    private String sourceXml() {
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <ceb:CEB311Message guid="tCEB311_HNZB_HNFX_20230707223752_003" version="1.0" xmlns:ceb="http://www.chinaport.gov.cn/ceb" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                    <ceb:Order>
                        <ceb:OrderHead>
                            <ceb:guid>tCEB311_HNZB_HNFX_20230707223752_001</ceb:guid>
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
                </ceb:CEB311Message>
                """;
    }
}
