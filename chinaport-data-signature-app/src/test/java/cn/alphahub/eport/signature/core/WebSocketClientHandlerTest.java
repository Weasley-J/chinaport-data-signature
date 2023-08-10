package cn.alphahub.eport.signature.core;

import cn.alphahub.eport.signature.entity.UkeyResponse;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebSocketClientHandlerTest {

    @Autowired
    WebSocketClientHandler handler;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("复位读卡器失败发送邮件通知")
    void handleTextMessage() {
        String ukeyErrJson = """
                {
                  "_id": 1,
                  "_method": "cus-sec_SpcSignDataAsPEM",
                  "_status": "00",
                  "_args": {
                    "Result": false,
                    "Data": [],
                    "Error": [
                      "[读卡器底层库]复位读卡器失败:错误码\\u003d50070",
                      "Err:Custom50070"
                    ]
                  }
                }
                """;
        String messagePayload = """
                <ceb:CEB311Message guid="YGD8P9-WEASLEY-20230805155746-HOUDF2" version="1.0" xmlns:ceb="http://www.chinaport.gov.cn/ceb" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" >
                    <ceb:Order>
                        <ceb:OrderHead>
                            <ceb:guid>YGD8P9-WEASLEY-20230805155746-HOUDF2</ceb:guid>
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
        UkeyResponse response = JSONUtil.toBean(ukeyErrJson, new TypeReference<>() {
        }, true);
        handler.sendAlertSignFailure(response, messagePayload);
    }
}
