package cn.alphahub.eport.signature.controller.rpc;

import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.core.ChinaEportReportClient;
import cn.alphahub.eport.signature.entity.Capture179DataRequest;
import cn.alphahub.eport.signature.entity.Capture179DataResponse;
import cn.alphahub.eport.signature.entity.ThirdAbstractResponse;
import cn.alphahub.eport.signature.entity.UploadCEBMessageRequest;
import lombok.extern.slf4j.Slf4j;
import o.github.weasleyj.china.eport.sign.model.request.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 电子口岸报文推送
 *
 * @author weasley
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/rpc/eport/upload")
public class EportUploadController {

    @Autowired
    private ChinaEportReportClient chinaEportReportClient;

    /**
     * 推送报文CEBMessage报文
     * <ul>
     *  CEB311Message推送XML报文示例:
     * <pre>
     * {
     *   "messageType": "CEB311Message",
     *   "dataType": "XML",
     *   "cebMessage": "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ceb:CEB311Message guid=\"tCEB311_HNZB_HNFX_20230707223752_003\" version=\"1.0\"\n                   xmlns:ceb=\"http://www.chinaport.gov.cn/ceb\">\n    <ceb:Order>\n        <ceb:OrderHead>\n            <ceb:guid>tCEB311_HNZB_HNFX_20230707223752_001</ceb:guid>\n            <ceb:appType>1</ceb:appType>\n            <ceb:appTime>20230704181028</ceb:appTime>\n            <ceb:appStatus>2</ceb:appStatus>\n            <ceb:orderType>I</ceb:orderType>\n            <ceb:orderNo>T_C5051511332138160010</ceb:orderNo>\n            <ceb:ebpCode>4601630004</ceb:ebpCode>\n            <ceb:ebpName>海南省荣誉进出口贸易有限公司</ceb:ebpName>\n            <ceb:ebcCode>4601630004</ceb:ebcCode>\n            <ceb:ebcName>海南省荣誉进出口贸易有限公司</ceb:ebcName>\n            <ceb:goodsValue>0.01</ceb:goodsValue>\n            <ceb:freight>0</ceb:freight>\n            <ceb:discount>0</ceb:discount>\n            <ceb:taxTotal>0</ceb:taxTotal>\n            <ceb:acturalPaid>0.01</ceb:acturalPaid>\n            <ceb:currency>142</ceb:currency>\n            <ceb:buyerRegNo>4</ceb:buyerRegNo>\n            <ceb:buyerName>袁晓雨</ceb:buyerName>\n            <ceb:buyerTelephone>13701727375</ceb:buyerTelephone>\n            <ceb:buyerIdType>1</ceb:buyerIdType>\n            <ceb:buyerIdNumber>130435200009241538</ceb:buyerIdNumber>\n            <ceb:consignee>袁晓雨</ceb:consignee>\n            <ceb:consigneeTelephone>13701727375</ceb:consigneeTelephone>\n            <ceb:consigneeAddress>北京北京市东城区</ceb:consigneeAddress>\n            <ceb:note>test</ceb:note>\n        </ceb:OrderHead>\n        <ceb:OrderList>\n            <ceb:gnum>1</ceb:gnum>\n            <ceb:itemNo>1</ceb:itemNo>\n            <ceb:itemName>LANNA兰纳</ceb:itemName>\n            <ceb:gmodel>10片/包</ceb:gmodel>\n            <ceb:itemDescribe></ceb:itemDescribe>\n            <ceb:barCode>1</ceb:barCode>\n            <ceb:unit>011</ceb:unit>\n            <ceb:qty>1</ceb:qty>\n            <ceb:price>1</ceb:price>\n            <ceb:totalPrice>1</ceb:totalPrice>\n            <ceb:currency>142</ceb:currency>\n            <ceb:country>136</ceb:country>\n            <ceb:note>test</ceb:note>\n        </ceb:OrderList>\n    </ceb:Order>\n    <ceb:BaseTransfer>\n        <ceb:copCode>4601630004</ceb:copCode>\n        <ceb:copName>海南省荣誉进出口贸易有限公司</ceb:copName>\n        <ceb:dxpMode>DXP</ceb:dxpMode>\n        <ceb:dxpId>DXPENT0000530815</ceb:dxpId>\n        <ceb:note>test</ceb:note>\n    </ceb:BaseTransfer>\n</ceb:CEB311Message>\n"
     * }
     * </pre>
     *  CEB311Message推送JSON报文示例:
     * <pre>
     * {
     *   "messageType": "CEB311Message",
     *   "dataType": "JSON",
     *   "cebMessage": "{\"order\":{\"orderHead\":{\"guid\":\"COV6H9-WEASLEY-20230713001707-YIBN8Q\",\"appType\":\"1\",\"appTime\":\"20230704181028\",\"appStatus\":\"2\",\"orderType\":\"I\",\"orderNo\":\"T_C5051511332138160010\",\"ebpCode\":\"4601630004\",\"ebpName\":\"海南省荣誉进出口贸易有限公司\",\"ebcCode\":\"4601630004\",\"ebcName\":\"海南省荣誉进出口贸易有限公司\",\"goodsValue\":\"0.01\",\"freight\":\"0\",\"discount\":\"0\",\"taxTotal\":\"0\",\"acturalPaid\":\"0.01\",\"currency\":\"142\",\"buyerRegNo\":\"4\",\"buyerName\":\"袁晓雨\",\"buyerTelephone\":\"13701727375\",\"buyerIdType\":\"1\",\"buyerIdNumber\":\"130435200009241538\",\"consignee\":\"袁晓雨\",\"consigneeTelephone\":\"13701727375\",\"consigneeAddress\":\"北京北京市东城区\",\"note\":\"test\"},\"orderList\":[{\"gnum\":1,\"itemNo\":\"1\",\"itemName\":\"LANNA兰纳\",\"gmodel\":\"10片/包\",\"itemDescribe\":\"\",\"barCode\":\"1\",\"unit\":\"011\",\"qty\":\"1\",\"price\":\"1\",\"totalPrice\":\"1\",\"currency\":\"142\",\"country\":\"136\",\"note\":\"test\"}]},\"guid\":\"COV6H9-WEASLEY-20230713001707-YIBN8Q\",\"version\":\"1.0\",\"baseTransfer\":{\"copCode\":\"4601630004\",\"copName\":\"海南省荣誉进出口贸易有限公司\",\"dxpMode\":\"DXP\",\"dxpId\":\"DXPENT0000530815\",\"note\":\"test\"}}"
     * }
     * </pre>
     * </ul>
     *
     * @param request CEBMessage报文
     * @return 结果，OK表示已推送
     * @apiNote <ul>
     * <b>支持CEBMessage的JSON报文和XML报文</b>
     * <li>JSON报文: 调用者无需组装XML结构报文，但需要将JSON报文进行字符串转义</li>
     * <li>XML报文: 调用者需组装XML结构的报文</li>
     * </ul>
     * @implSpec
     */
    @PostMapping("/CEBMessage")
    public Result<ThirdAbstractResponse<MessageRequest, String, String>> uploadCEBMessage(@RequestBody @Validated UploadCEBMessageRequest request) {
        ThirdAbstractResponse<MessageRequest, String, String> report = chinaEportReportClient.report(chinaEportReportClient.getCebMessageByMessageType(request), request.getMessageType());
        return Result.ok(report);
    }

    /**
     * 海关179数据抓取
     * <ul>
     *  json示例:
     * <pre>
     * {
     *   "sessionID": "032C3F56-0EE6-4558-B548-6C7A3451F07D",
     *   "payExchangeInfoHead": {
     *     "guid": "E4766021-21AC-1AA2-21DD-E974DF93C11D",
     *     "initalRequest": "<xml><appid><![CDATA[xwxwxwxwx112121]]></appid><mch_id>11111111</mch_id><body><![CDATA[测试测试]]></body><nonce_str><![CDATA[ashdgahgdhaghgliqwueyqu1]]></nonce_str><out_trade_no>202009231454212140210352</out_trade_no><total_fee>500</total_fee><spbill_create_ip><![CDATA[192.168.0.1]]></spbill_create_ip><notify_url><![CDATA[https://www.baidu.com]]></notify_url><openid><![CDATA[adasyduiyasdhjxzycua-Q]]></openid><trade_type><![CDATA[JSAPI]]></trade_type><sign><![CDATA[asd1sa56d4545wqe44wq5]]></sign></xml>",
     *     "initalResponse": "<xml><appid><![CDATA[xwxwxwxwx112121]]></appid><bank_type><![CDATA[OTHERS]]></bank_type><cash_fee>500</cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id>11111111</mch_id><nonce_str><![CDATA[ashdgahgdhaghgliqwueyqu1]]></nonce_str><openid><![CDATA[adasyduiyasdhjxzycua-Q]]></openid><out_trade_no>202009231454212140210352</out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[asgdhasgdhasgdhgasgdhasgdh]]></sign><time_end>20200923145426</time_end><total_fee>500</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id>4200000681202009235085032319</transaction_id></xml>",
     *     "ebpCode": "请替换",
     *     "payCode": "4403169D3W",
     *     "payTransactionId": "4200000681202009235085032319",
     *     "totalAmount": 5,
     *     "currency": "142",
     *     "verDept": "3",
     *     "payType": "4",
     *     "tradingTime": "20200923145426",
     *     "note": ""
     *   },
     *   "payExchangeInfoLists": [
     *     {
     *       "orderNo": "202009231454218421271832",
     *       "goodsInfo": [
     *         {
     *           "gname": "济州花梨精华面膜",
     *           "itemLink": "http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999761&shopId=453"
     *         }
     *       ],
     *       "recpAccount": "请替换",
     *       "recpCode": "请替换",
     *       "recpName": "请替换"
     *     }
     *   ],
     *   "serviceTime": "1601282210417"
     * }
     * </pre>
     * </ul>
     */
    @PostMapping("/179/data")
    public Result<ThirdAbstractResponse<Map<String, Object>, String, Capture179DataResponse>> capture179Data(@RequestBody @Validated Capture179DataRequest request) {
        ThirdAbstractResponse<Map<String, Object>, String, Capture179DataResponse> report = chinaEportReportClient.capture179Data(request);
        return Result.ok(report);
    }

}
