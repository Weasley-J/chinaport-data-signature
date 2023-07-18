package cn.alphahub.eport.signature.c14;


import cn.alphahub.eport.signature.core.SignatureHandler;
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
class C14nEncodeTests {

    @Test
    void testSignCEB311MessageWithC14n() {
        org.apache.xml.security.Init.init();
        String sourceXml = """
                <ceb:CEB311Message xmlns:ceb="http://www.chinaport.gov.cn/ceb" guid="4CDE1CFD-EDED-46B1-946C-B8022E42FC94"
                                   version="1.0">
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
        String messageWithC14n = SignatureHandler.getDigestValueOfCEBXxxMessage(sourceXml);
        System.err.println(messageWithC14n);
        assert "vDf4oKPVFtRft5U57TNW85kJT18=".equals(messageWithC14n);
    }

    @Test
    void testSha1HexCEB621Message() {
        String xml = """
                <ceb:CEB621Message xmlns:ceb="http://www.chinaport.gov.cn/ceb" guid="CEB621_HNZB_FXJK_20220208175054_0034"
                                   version="v1.0">
                    <ceb:Inventory>
                        <ceb:InventoryHead>
                            <ceb:guid>CEB621_HNZB_FXJK_20220208175055_0035</ceb:guid>
                            <ceb:appType>1</ceb:appType>
                            <ceb:appTime>20220208175055</ceb:appTime>
                            <ceb:appStatus>2</ceb:appStatus>
                            <ceb:orderNo>48037605991776256</ceb:orderNo>
                            <ceb:ebpCode>46121601BC</ceb:ebpCode>
                            <ceb:ebpName>海南星创互联网医药有限公司</ceb:ebpName>
                            <ceb:ebcCode>46121601BC</ceb:ebcCode>
                            <ceb:ebcName>海南星创互联网医药有限公司</ceb:ebcName>
                            <ceb:logisticsNo>2222222</ceb:logisticsNo>
                            <ceb:logisticsCode>4101986180</ceb:logisticsCode>
                            <ceb:logisticsName>圆通</ceb:logisticsName>
                            <ceb:copNo>4101W68006</ceb:copNo>
                            <ceb:assureCode>4101960AGP</ceb:assureCode>
                            <ceb:ieFlag>I</ceb:ieFlag>
                            <ceb:declTime>20220208</ceb:declTime>
                            <ceb:customsCode>4601</ceb:customsCode>
                            <ceb:portCode>4601</ceb:portCode>
                            <ceb:ieDate>20220208</ceb:ieDate>
                            <ceb:buyerIdType>1</ceb:buyerIdType>
                            <ceb:buyerIdNumber>612326199005306719</ceb:buyerIdNumber>
                            <ceb:buyerName>用户_15150368</ceb:buyerName>
                            <ceb:buyerTelephone>18209182500</ceb:buyerTelephone>
                            <ceb:consigneeAddress>哈布斯堡</ceb:consigneeAddress>
                            <ceb:agentCode>46121601BC</ceb:agentCode>
                            <ceb:agentName>海南星创互联网医药有限公司</ceb:agentName>
                            <ceb:tradeMode>9610</ceb:tradeMode>
                            <ceb:trafMode>W</ceb:trafMode>
                            <ceb:country>142</ceb:country>
                            <ceb:freight>0.00</ceb:freight>
                            <ceb:insuredFee>0</ceb:insuredFee>
                            <ceb:currency>142</ceb:currency>
                            <ceb:packNo>1</ceb:packNo>
                            <ceb:grossWeight>0.07</ceb:grossWeight>
                            <ceb:netWeight>0.06</ceb:netWeight>
                        </ceb:InventoryHead>
                        <ceb:InventoryList>
                            <ceb:gnum>1</ceb:gnum>
                            <ceb:itemRecordNo>0797776159482</ceb:itemRecordNo>
                            <ceb:itemNo>HNGGKJ00000057</ceb:itemNo>
                            <ceb:itemName>通用铁剂胶囊</ceb:itemName>
                            <ceb:gcode>2106909090</ceb:gcode>
                            <ceb:gname>ACTIVE IRON</ceb:gname>
                            <ceb:gmodel>4|3|乳清蛋白(牛奶)65%,胶囊壳(羟苯基甲基纤维素)21%,硫酸亚铁1.2%,酸度调节剂2%,填充剂 10.8%|30粒/盒|无中文名称/ACTIVE IRON</ceb:gmodel>
                            <ceb:country>306</ceb:country>
                            <ceb:currency>142</ceb:currency>
                            <ceb:qty>1</ceb:qty>
                            <ceb:unit>140</ceb:unit>
                            <ceb:qty1>0.06</ceb:qty1>
                            <ceb:unit1>035</ceb:unit1>
                            <ceb:price>0.01</ceb:price>
                            <ceb:totalPrice>0.01</ceb:totalPrice>
                        </ceb:InventoryList>
                    </ceb:Inventory>
                    <ceb:BaseTransfer>
                        <ceb:copCode>46121601BC</ceb:copCode>
                        <ceb:copName>海南星创互联网医药有限公司</ceb:copName>
                        <ceb:dxpMode>DXP</ceb:dxpMode>
                        <ceb:dxpId>DXPENT0000458763</ceb:dxpId>
                    </ceb:BaseTransfer>
                </ceb:CEB621Message>
                """;
        System.out.println(xml);
    }

}
