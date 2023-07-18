package cn.alphahub.eport.signature.after202207;

/**
 * SignConstant
 *
 * @author weasley
 * @version 1.0.0
 */
public interface SignConstant {
    /**
     * 179入参示例
     */
    String sign179 = """
            "sessionID":"ad2254-8hewyf32-55616249"||"payExchangeInfoHead":"{"guid":"9D55BA71-22DE-41F4-8B50-C36C83B3B530","initalRequest":"原始请求","initalResponse":"ok","ebpCode":"4404840022","payCode":"312226T001","payTransactionId":"2018121222001354081010726129","totalAmount":100,"currency":"142","verDept":"3","payType":"1","tradingTime":"20181212041803","note":"批量订单，测试订单优化,生成多个so订单"}"||"payExchangeInfoLists":"[{"orderNo":"SO1710301150602574003","goodsInfo":[{"gname":"lhy-gnsku3","itemLink":"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999761&shopId=453"},{"gname":"lhy-gnsku2","itemLink":"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999760&shopId=453"}],"recpAccount":"OSA571908863132601","recpCode":"","recpName":"YUNJIHONGKONGLIMITED"}]"||"serviceTime":"1544519952469"
            """;

    /**
     * XML入参示例
     */
    String signXml = """
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
}
