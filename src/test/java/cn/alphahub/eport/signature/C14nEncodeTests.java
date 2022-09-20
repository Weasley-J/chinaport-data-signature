package cn.alphahub.eport.signature;


import cn.alphahub.eport.signature.core.SignatureHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Sign CEB311Message With C14n
 * <p>
 *
 * @author lwj
 * @version 1.0
 * @date 2022-01-11 14:23
 */
@SpringBootTest
public class C14nEncodeTests {

    @Test
    void testSignCEB311MessageWithC14n() {
        org.apache.xml.security.Init.init();
        String sourceXml = "<ceb:CEB311Message xmlns:ceb=\"http://www.chinaport.gov.cn/ceb\" guid=\"4CDE1CFD-EDED-46B1-946C-B8022E42FC94\"\n" +
                "                   version=\"1.0\">\n" +
                "    <link type=\"text/css\" id=\"dark-mode\" rel=\"stylesheet\" href=\"\"/>\n" +
                "    <ceb:Order>\n" +
                "        <ceb:OrderHead>\n" +
                "            <ceb:guid>4CDE1CFD-EDED-46B1-946C-B8022E42FC94</ceb:guid>\n" +
                "            <ceb:appType>1</ceb:appType>\n" +
                "            <ceb:appTime>20160308112701</ceb:appTime>\n" +
                "            <ceb:appStatus>2</ceb:appStatus>\n" +
                "            <ceb:orderType>I</ceb:orderType>\n" +
                "            <ceb:orderNo>order20160321116421002</ceb:orderNo>\n" +
                "            <ceb:ebpCode>1105910159</ceb:ebpCode>\n" +
                "            <ceb:ebpName>东方物通科技(北京)有限公司</ceb:ebpName>\n" +
                "            <ceb:ebcCode>1105910159</ceb:ebcCode>\n" +
                "            <ceb:ebcName>东方物通科技(北京)有限公司</ceb:ebcName>\n" +
                "            <ceb:goodsValue>14000</ceb:goodsValue>\n" +
                "            <ceb:freight>5000</ceb:freight>\n" +
                "            <ceb:discount>0</ceb:discount>\n" +
                "            <ceb:taxTotal>50</ceb:taxTotal>\n" +
                "            <ceb:acturalPaid>19050</ceb:acturalPaid>\n" +
                "            <ceb:currency>142</ceb:currency>\n" +
                "            <ceb:buyerRegNo>ID20160001</ceb:buyerRegNo>\n" +
                "            <ceb:buyerName>aa</ceb:buyerName>\n" +
                "            <ceb:buyerTelephone>18813025940</ceb:buyerTelephone>\n" +
                "            <ceb:buyerIdType>1</ceb:buyerIdType>\n" +
                "            <ceb:buyerIdNumber>130681136250023332</ceb:buyerIdNumber>\n" +
                "            <ceb:payCode>1105910159</ceb:payCode>\n" +
                "            <ceb:payName>东方物通科技(北京)有限公司</ceb:payName>\n" +
                "            <ceb:payTransactionId>20160001634226001</ceb:payTransactionId>\n" +
                "            <ceb:batchNumbers>20160317</ceb:batchNumbers>\n" +
                "            <ceb:consignee>焦洪宇</ceb:consignee>\n" +
                "            <ceb:consigneeTelephone>13522652231</ceb:consigneeTelephone>\n" +
                "            <ceb:consigneeAddress>北京市海淀区</ceb:consigneeAddress>\n" +
                "            <ceb:consigneeDistrict>072750</ceb:consigneeDistrict>\n" +
                "            <ceb:note>test</ceb:note>\n" +
                "        </ceb:OrderHead>\n" +
                "        <ceb:OrderList>\n" +
                "            <ceb:gnum>1</ceb:gnum>\n" +
                "            <ceb:itemNo>AF001-001</ceb:itemNo>\n" +
                "            <ceb:itemName>b</ceb:itemName>\n" +
                "            <ceb:gmodel>33mm</ceb:gmodel>\n" +
                "            <ceb:itemDescribe>v</ceb:itemDescribe>\n" +
                "            <ceb:barCode>2345123</ceb:barCode>\n" +
                "            <ceb:unit>007</ceb:unit>\n" +
                "            <ceb:qty>100</ceb:qty>\n" +
                "            <ceb:price>20</ceb:price>\n" +
                "            <ceb:totalPrice>2000</ceb:totalPrice>\n" +
                "            <ceb:currency>142</ceb:currency>\n" +
                "            <ceb:country>116</ceb:country>\n" +
                "            <ceb:note/>\n" +
                "        </ceb:OrderList>\n" +
                "    </ceb:Order>\n" +
                "    <ceb:BaseTransfer>\n" +
                "        <ceb:copCode>1101180326</ceb:copCode>\n" +
                "        <ceb:copName>物流企业</ceb:copName>\n" +
                "        <ceb:dxpMode>DXP</ceb:dxpMode>\n" +
                "        <ceb:dxpId>EXP2016522002580001</ceb:dxpId>\n" +
                "        <ceb:note>test</ceb:note>\n" +
                "    </ceb:BaseTransfer>\n" +
                "</ceb:CEB311Message>";
        String messageWithC14n = SignatureHandler.getDigestValueOfCEBXxxMessage(sourceXml);
        System.err.println(messageWithC14n);
        assert "vDf4oKPVFtRft5U57TNW85kJT18=".equals(messageWithC14n);
    }

    @Test
    void testSha1Hex() {
        String xml = "<ceb:CEB621Message xmlns:ceb=\"http://www.chinaport.gov.cn/ceb\" guid=\"CEB621_HNZB_FXJK_20220208175054_0034\"\n" +
                "                   version=\"v1.0\">\n" +
                "    <ceb:Inventory>\n" +
                "        <ceb:InventoryHead>\n" +
                "            <ceb:guid>CEB621_HNZB_FXJK_20220208175055_0035</ceb:guid>\n" +
                "            <ceb:appType>1</ceb:appType>\n" +
                "            <ceb:appTime>20220208175055</ceb:appTime>\n" +
                "            <ceb:appStatus>2</ceb:appStatus>\n" +
                "            <ceb:orderNo>48037605991776256</ceb:orderNo>\n" +
                "            <ceb:ebpCode>46121601BC</ceb:ebpCode>\n" +
                "            <ceb:ebpName>海南星创互联网医药有限公司</ceb:ebpName>\n" +
                "            <ceb:ebcCode>46121601BC</ceb:ebcCode>\n" +
                "            <ceb:ebcName>海南星创互联网医药有限公司</ceb:ebcName>\n" +
                "            <ceb:logisticsNo>2222222</ceb:logisticsNo>\n" +
                "            <ceb:logisticsCode>4101986180</ceb:logisticsCode>\n" +
                "            <ceb:logisticsName>圆通</ceb:logisticsName>\n" +
                "            <ceb:copNo>4101W68006</ceb:copNo>\n" +
                "            <ceb:assureCode>4101960AGP</ceb:assureCode>\n" +
                "            <ceb:ieFlag>I</ceb:ieFlag>\n" +
                "            <ceb:declTime>20220208</ceb:declTime>\n" +
                "            <ceb:customsCode>4601</ceb:customsCode>\n" +
                "            <ceb:portCode>4601</ceb:portCode>\n" +
                "            <ceb:ieDate>20220208</ceb:ieDate>\n" +
                "            <ceb:buyerIdType>1</ceb:buyerIdType>\n" +
                "            <ceb:buyerIdNumber>612326199005306719</ceb:buyerIdNumber>\n" +
                "            <ceb:buyerName>用户_15150368</ceb:buyerName>\n" +
                "            <ceb:buyerTelephone>18209182500</ceb:buyerTelephone>\n" +
                "            <ceb:consigneeAddress>哈布斯堡</ceb:consigneeAddress>\n" +
                "            <ceb:agentCode>46121601BC</ceb:agentCode>\n" +
                "            <ceb:agentName>海南星创互联网医药有限公司</ceb:agentName>\n" +
                "            <ceb:tradeMode>9610</ceb:tradeMode>\n" +
                "            <ceb:trafMode>W</ceb:trafMode>\n" +
                "            <ceb:country>142</ceb:country>\n" +
                "            <ceb:freight>0.00</ceb:freight>\n" +
                "            <ceb:insuredFee>0</ceb:insuredFee>\n" +
                "            <ceb:currency>142</ceb:currency>\n" +
                "            <ceb:packNo>1</ceb:packNo>\n" +
                "            <ceb:grossWeight>0.07</ceb:grossWeight>\n" +
                "            <ceb:netWeight>0.06</ceb:netWeight>\n" +
                "        </ceb:InventoryHead>\n" +
                "        <ceb:InventoryList>\n" +
                "            <ceb:gnum>1</ceb:gnum>\n" +
                "            <ceb:itemRecordNo>0797776159482</ceb:itemRecordNo>\n" +
                "            <ceb:itemNo>HNGGKJ00000057</ceb:itemNo>\n" +
                "            <ceb:itemName>通用铁剂胶囊</ceb:itemName>\n" +
                "            <ceb:gcode>2106909090</ceb:gcode>\n" +
                "            <ceb:gname>ACTIVE IRON</ceb:gname>\n" +
                "            <ceb:gmodel>4|3|乳清蛋白(牛奶)65%,胶囊壳(羟苯基甲基纤维素)21%,硫酸亚铁1.2%,酸度调节剂2%,填充剂 10.8%|30粒/盒|无中文名称/ACTIVE IRON</ceb:gmodel>\n" +
                "            <ceb:country>306</ceb:country>\n" +
                "            <ceb:currency>142</ceb:currency>\n" +
                "            <ceb:qty>1</ceb:qty>\n" +
                "            <ceb:unit>140</ceb:unit>\n" +
                "            <ceb:qty1>0.06</ceb:qty1>\n" +
                "            <ceb:unit1>035</ceb:unit1>\n" +
                "            <ceb:price>0.01</ceb:price>\n" +
                "            <ceb:totalPrice>0.01</ceb:totalPrice>\n" +
                "        </ceb:InventoryList>\n" +
                "    </ceb:Inventory>\n" +
                "    <ceb:BaseTransfer>\n" +
                "        <ceb:copCode>46121601BC</ceb:copCode>\n" +
                "        <ceb:copName>海南星创互联网医药有限公司</ceb:copName>\n" +
                "        <ceb:dxpMode>DXP</ceb:dxpMode>\n" +
                "        <ceb:dxpId>DXPENT0000458763</ceb:dxpId>\n" +
                "    </ceb:BaseTransfer>\n" +
                "</ceb:CEB621Message>";
    }

}
