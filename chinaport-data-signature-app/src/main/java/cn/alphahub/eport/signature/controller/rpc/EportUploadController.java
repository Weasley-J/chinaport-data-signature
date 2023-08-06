package cn.alphahub.eport.signature.controller.rpc;

import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.core.ChinaEportReportClient;
import cn.alphahub.eport.signature.entity.Capture179DataRequest;
import cn.alphahub.eport.signature.entity.Capture179DataResponse;
import cn.alphahub.eport.signature.entity.ThirdAbstractResponse;
import cn.alphahub.eport.signature.entity.UploadCEBMessageRequest;
import io.github.weasleyj.china.eport.sign.AbstractCebMessage;
import io.github.weasleyj.china.eport.sign.model.request.MessageRequest;
import lombok.extern.slf4j.Slf4j;
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
     * 推送CEBMessage报文
     *
     * @param request CEBMessage报文
     * @return 结果，OK 表示已推送
     * @apiNote <ul>
     * <b>支持JSON报文和XML报文</b>
     * <li>JSON报文: 无需组装XML结构报文</li>
     * <li>XML报文: 需组装XML结构的报文</li>
     * </ul>
     */
    @PostMapping("/CEBMessage")
    public Result<ThirdAbstractResponse<MessageRequest, String, String>> uploadCEBMessage(@RequestBody @Validated UploadCEBMessageRequest request) {
        AbstractCebMessage messageType = chinaEportReportClient.buildCebMessage(request);
        ThirdAbstractResponse<MessageRequest, String, String> report = chinaEportReportClient.report(messageType, request.getMessageType());
        return Result.ok(report);
    }

    /**
     * 推送海关179号数据
     *
     * @apiNote <ul>
     * JSON示例:
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
