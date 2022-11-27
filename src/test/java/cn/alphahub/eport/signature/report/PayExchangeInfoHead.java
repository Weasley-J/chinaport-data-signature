package cn.alphahub.eport.signature.report;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayExchangeInfoHead implements Serializable {

    /**
     * 系统唯一序号
     */
    private String guid;

    /**
     * 原始请求
     */
    private String initalRequest;

    /**
     * 原始响应
     */
    private String initalResponse;

    /**
     * 电商平台代码
     */
    private String ebpCode;

    /**
     * 支付企业代码
     */
    private String payCode;

    /**
     * 交易流水号(支付报关时会有返回)
     * 微信对应的字段是:verify_department_trade_id
     * 支付宝对应的字段是:pay_transaction_id
     */
    private String payTransactionId;

    /**
     * 交易金额
     */
    private Double totalAmount;

    /**
     * 币制(币制编码:142(人民币))
     */
    private String currency = "142";

    /**
     * 验核机构(支付报关结果会返回),1-银联 2-网联 3-其他
     * 微信返回的字段是:verify_department
     * 支付宝对应的字段是:ver_dept
     */
    private String verDept;

    /**
     * 支付类型
     */
    private String payType;

    /**
     * 交易成功时间
     */
    private String tradingTime;

    /**
     * 备注
     */
    private String note;
}
