package cn.alphahub.eport.signature.feign;

import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.config.UkeyAccessClientProperties.Command;
import cn.alphahub.eport.signature.entity.Capture179DataRequest;
import cn.alphahub.eport.signature.entity.Capture179DataResponse;
import cn.alphahub.eport.signature.entity.ConsoleOutput;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.entity.ThirdAbstractResponse;
import cn.alphahub.eport.signature.entity.UploadCEBMessageRequest;
import jakarta.servlet.http.HttpServletResponse;
import o.github.weasleyj.china.eport.sign.model.request.MessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Map;

import static cn.alphahub.eport.signature.feign.ChinaEportSignatureFeignClient.NAME;

/**
 * China Eport Signature Feign Client
 *
 * @author weasley
 * @version 1.1.0
 */
@FeignClient(name = NAME)
public interface ChinaEportSignatureFeignClient {
    String NAME = "chinaport-data-signature";

    /**
     * 下载证书
     *
     * @apiNote 证书文件格式: 证书编号.cer, 遇到项目启动的首页下载证书出现文件名为 unknown.cer 的情况将下载链接复制到浏览器中打开
     */
    @GetMapping("/rpc/eport/cert/download")
    void downloadX509Certificate(HttpServletResponse response) throws IOException;

    /**
     * 海关数据加签
     *
     * @param request 加签数据请求入参
     * @return 签名结果
     * @apiNote 此接口已经整合"海关总署XML"和"海关179数据抓取"的加签<br/>
     * <ul><b>支持的加签类型</b><li>1. 海关CEBXxxMessage XML数据加签</li><li>2. 海关179数据加签</li></ul>
     */
    @PostMapping("/rpc/eport/signature")
    Result<SignResult> signature(@RequestBody @Validated SignRequest request);

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
    @PostMapping("/rpc/eport/upload/CEBMessage")
    Result<ThirdAbstractResponse<MessageRequest, String, String>> uploadCEBMessage(@RequestBody @Validated UploadCEBMessageRequest request);

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
    @PostMapping("/rpc/eport/upload/179/data")
    Result<ThirdAbstractResponse<Map<String, Object>, String, Capture179DataResponse>> capture179Data(@RequestBody @Validated Capture179DataRequest request);

    /**
     * ukey健康指令
     *
     * @return 控制太输出
     * @apiNote <ul>
     * 支持command类型
     * <li>RESTART: 重启控件(较多使用)</li>
     * <li>START: 启动控件</li>
     * <li>STOP: 停止控件</li>
     * <li>REPAIR: 修复证书</li>
     * </ul>
     * @see Command
     */
    @PostMapping("/rpc/eport/ukey/health/endpoint/{command}")
    Result<ConsoleOutput> endpoint(@PathVariable("command") Command command);

}
