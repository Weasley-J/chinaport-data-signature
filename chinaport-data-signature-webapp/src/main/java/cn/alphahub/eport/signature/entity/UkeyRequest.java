package cn.alphahub.eport.signature.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Map;

/**
 * Ukey Request
 * <p>
 * 入参示例之一:
 * <pre>
 * {
 *   "_method": "cus-sec_SpcSignDataAsPEM",
 *   "_id": 1,
 *   "args": {
 *     "inData": "sessionID":"ad2254-8hewyf32-55616249"||"payExchangeInfoHead":"{"guid":"9D55BA71-22DE-41F4-8B50-C36C83B3B530","initalRequest":"原始请求","initalResponse":"ok","ebpCode":"4404840022","payCode":"312226T001","payTransactionId":"2018121222001354081010726129","totalAmount":100,"currency":"142","verDept":"3","payType":"1","tradingTime":"20181212041803","note":"批量订单，测试订单优化,生成多个so订单"}"||"payExchangeInfoLists":"[{"orderNo":"SO1710301150602574003","goodsInfo":[{"gname":"lhy-gnsku3","itemLink":"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999761&shopId=453"},{"gname":"lhy-gnsku2","itemLink":"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999760&shopId=453"}],"recpAccount":"OSA571908863132601","recpCode":"","recpName":"YUNJIHONGKONGLIMITED"}]"||"serviceTime":"1544519952469",
 *     "passwd": "88888888"
 *   }
 * }
 * </pre>
 *
 * @author weasley
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UkeyRequest implements Serializable {
    private String _method;
    private Integer _id = 1;
    @Nullable
    private Map<String, Object> args;

    public UkeyRequest(String _method, @Nullable Map<String, Object> args) {
        this._method = _method;
        this.args = args;
    }
}
