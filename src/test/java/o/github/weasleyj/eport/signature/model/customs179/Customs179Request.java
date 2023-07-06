package o.github.weasleyj.eport.signature.model.customs179;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 179上报请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Customs179Request implements Serializable {
    /**
     * 海关发起请求时，平台接收的会话ID
     */
    private String sessionID;

    /**
     * 支付原始数据表头
     */
    private PayExchangeInfoHead payExchangeInfoHead;

    /**
     * 支付原始数据表体
     */
    private List<PayExchangeInfoBody> payExchangeInfoLists;

    /**
     * 返回时的系统时间
     */
    private String serviceTime;

    /**
     * 证书编号
     */
    private String certNo;

    /**
     * 签名结果值
     */
    private String signValue;

}
