package cn.alphahub.eport.signature.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 上报179请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report179Request implements Serializable {

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
