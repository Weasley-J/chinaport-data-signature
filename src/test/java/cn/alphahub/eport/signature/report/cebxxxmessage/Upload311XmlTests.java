package cn.alphahub.eport.signature.report.cebxxxmessage;

import lombok.extern.slf4j.Slf4j;
import o.github.weasleyj.eport.signature.constants.MessageType;
import o.github.weasleyj.eport.signature.model.cebmessage.CEB311Message;
import o.github.weasleyj.eport.signature.util.GUIDUtil;
import o.github.weasleyj.eport.signature.util.JAXBUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static cn.alphahub.dtt.plus.util.JacksonUtil.toJson;

/**
 * 311 进口单 xml 上报测试
 *
 * @author weasley
 * @version 1.0.0
 */
@Slf4j
@SpringBootTest
class Upload311XmlTests {

    @Autowired
    ChinaEportReportClient chinaEportReportClient;

    @Test
    @DisplayName("311 进口单 xml 上报测试")
    void push() {
        String sourceXml = """
                <ceb:CEB311Message xmlns:ceb="http://www.chinaport.gov.cn/ceb" guid="4CDE1CFD-EDED-46B1-946C-B8022E42FC94" version="1.0">
                    <link type="text/css" id="dark-mode" rel="stylesheet" href=""/>
                    <ceb:Order>
                        <ceb:OrderHead>
                            <ceb:guid>4CDE1CFD-EDED-46B1-946C-B8022E42FC94</ceb:guid>
                            <ceb:appType>1</ceb:appType>
                            <ceb:appTime>20160308112701</ceb:appTime>
                            <ceb:appStatus>2</ceb:appStatus>
                            <ceb:orderType>I</ceb:orderType>
                            <ceb:orderNo>order20160321116421002</ceb:orderNo>
                            <ceb:ebpCode>1105910159</ceb:ebpCode>
                            <ceb:ebpName>东方物通科技(北京)有限公司</ceb:ebpName>
                            <ceb:ebcCode>1105910159</ceb:ebcCode>
                            <ceb:ebcName>东方物通科技(北京)有限公司</ceb:ebcName>
                            <ceb:goodsValue>14000</ceb:goodsValue>
                            <ceb:freight>5000</ceb:freight>
                            <ceb:discount>0</ceb:discount>
                            <ceb:taxTotal>50</ceb:taxTotal>
                            <ceb:acturalPaid>19050</ceb:acturalPaid>
                            <ceb:currency>142</ceb:currency>
                            <ceb:buyerRegNo>ID20160001</ceb:buyerRegNo>
                            <ceb:buyerName>aa</ceb:buyerName>
                            <ceb:buyerTelephone>18813025940</ceb:buyerTelephone>
                            <ceb:buyerIdType>1</ceb:buyerIdType>
                            <ceb:buyerIdNumber>130681136250023332</ceb:buyerIdNumber>
                            <ceb:payCode>1105910159</ceb:payCode>
                            <ceb:payName>东方物通科技(北京)有限公司</ceb:payName>
                            <ceb:payTransactionId>20160001634226001</ceb:payTransactionId>
                            <ceb:batchNumbers>20160317</ceb:batchNumbers>
                            <ceb:consignee>焦洪宇</ceb:consignee>
                            <ceb:consigneeTelephone>13522652231</ceb:consigneeTelephone>
                            <ceb:consigneeAddress>北京市海淀区</ceb:consigneeAddress>
                            <ceb:consigneeDistrict>072750</ceb:consigneeDistrict>
                            <ceb:note>test</ceb:note>
                        </ceb:OrderHead>
                        <ceb:OrderList>
                            <ceb:gnum>1</ceb:gnum>
                            <ceb:itemNo>AF001-001</ceb:itemNo>
                            <ceb:itemName>b</ceb:itemName>
                            <ceb:gmodel>33mm</ceb:gmodel>
                            <ceb:itemDescribe>v</ceb:itemDescribe>
                            <ceb:barCode>2345123</ceb:barCode>
                            <ceb:unit>007</ceb:unit>
                            <ceb:qty>100</ceb:qty>
                            <ceb:price>20</ceb:price>
                            <ceb:totalPrice>2000</ceb:totalPrice>
                            <ceb:currency>142</ceb:currency>
                            <ceb:country>116</ceb:country>
                            <ceb:note/>
                        </ceb:OrderList>
                    </ceb:Order>
                    <ceb:BaseTransfer>
                        <ceb:copCode>1101180326</ceb:copCode>
                        <ceb:copName>物流企业</ceb:copName>
                        <ceb:dxpMode>DXP</ceb:dxpMode>
                        <ceb:dxpId>EXP2016522002580001</ceb:dxpId>
                        <ceb:note>test</ceb:note>
                    </ceb:BaseTransfer>
                </ceb:CEB311Message>
                """;

        CEB311Message ceb311Message = JAXBUtil.toBean(sourceXml, CEB311Message.class);
        assert ceb311Message != null;
        String guid = GUIDUtil.getGuid();
        ceb311Message.setGuid(guid);
        ceb311Message.getOrder().getOrderHead().setGuid(guid);
        ceb311Message.setVersion("1.0");
        ceb311Message.setBaseTransfer(chinaEportReportClient.buildBaseTransfer()); //参数需要替换成自己企业的

        System.out.println(toJson(ceb311Message));
        String xml = JAXBUtil.toXml(ceb311Message);

        chinaEportReportClient.report(ceb311Message, MessageType.CEB311Message);
    }
}
