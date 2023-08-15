package cn.alphahub.eport.signature.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * 加签数据请求入参
 *
 * @author weasley
 * @version 1.0
 * @date 2022/2/13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SignRequest implements Serializable {
    /**
     * 唯一id, 用来区分是哪一次发送的消息，默认值=1，从1开始，{@code int} 最大, 2<sup>31</sup>-1.
     */
    @Nullable
    @Max(Integer.MAX_VALUE)
    private Integer id = 1;
    /**
     * 加签源数据
     * <ul>
     *     <b>支持的加签数据类型</b>
     *     <li>1. 不带ds:Signature节点的CEBXxxMessage.xml原文;<a href='http://tool.qdhuaxun.cn/ceb/CEB311Message.xml'>待加签xml报文样例</a></li>
     *     <li>2. 海关179加签数据; 数据格式请传符合「海关179数据规范」的标准字符串,入参示例:
     *      <code><pre>"sessionID":"ad2254-8hewyf32-55616249"||"payExchangeInfoHead":"{"guid":"9D55BA71-22DE-41F4-8B50-C36C83B3B530","initalRequest":"原始请求","initalResponse":"ok","ebpCode":"4404840022","payCode":"312226T001","payTransactionId":"2018121222001354081010726129","totalAmount":100,"currency":"142","verDept":"3","payType":"1","tradingTime":"20181212041803","note":"批量订单，测试订单优化,生成多个so订单"}"||"payExchangeInfoLists":"[{"orderNo":"SO1710301150602574003","goodsInfo":[{"gname":"lhy-gnsku3","itemLink":"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999761&shopId=453"},{"gname":"lhy-gnsku2","itemLink":"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999760&shopId=453"}],"recpAccount":"OSA571908863132601","recpCode":"","recpName":"YUNJIHONGKONGLIMITED"}]"||"serviceTime":"1544519952469"</pre></code>
     *     </li>
     * </ul>
     */
    @NotBlank(message = "加签数据不能为空")
    private String data;

    public SignRequest(String data) {
        this.data = data;
    }

    @Nullable
    public Integer getId() {
        return (id == null || id <= 0) ? 1 : id;
    }
}
