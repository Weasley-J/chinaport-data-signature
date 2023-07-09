package cn.alphahub.eport.signature.config;

import cn.alphahub.eport.signature.core.ChinaEportReportClient;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * 电子口岸报文传输的企业元数据配置
 *
 * @author weasley
 * @version 1.1.0
 */
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = ChinaEportProperties.PREFIX)
public class ChinaEportProperties {
    public static final String PREFIX = "eport.signature.report";
    /**
     * 传输企业代码，报文传输的企业代码(需要与接入客户端的企 业身份一致)
     */
    @NotBlank(message = "配置文件中的报文传输的企业代码不能为空")
    private String copCode;
    /**
     * 传输企业名称，报文传输的企业名称
     */
    @NotBlank(message = "报文传输的企业名称不能为空")
    private String copName;
    /**
     * 报文传输编号，向中国电子口岸数据中心申请数据交换平台的用户编号
     */
    @NotBlank(message = "向中国电子口岸数据中心申请数据交换平台的用户编号不能为空")
    private String dxpId;
    /**
     * 海关服务器地址，缺省则采用Client中的密文作文默认Server URL
     *
     * @apiNote CEMMessage XML数据上报服务器地址，格式: http://ip:port
     * @see ChinaEportReportClient#EPORT_CEBMESSAGE_SERVER_ENCODE
     */
    private String server;

    /* 注释以下参数，一般用不到 */
    //private String emsNo;
    //private String areaName;
    ///**
    // * e贸接口域名 推送订单
    // */
    //private String hainanXCUrl;
    ///**
    // * 电商平台代码 电商平台的海关注册登记编 号或统一社会信用代码。
    // */
    //private String ebpCode;
    ///**
    // * 电商平台名称 电商平台的登记名称。
    // */
    //private String ebpName;
    ///**
    // * 电商企业代码 电商企业的海关注册登记编
    // * 号或统一社会信用代码，对应 清单的收发货人。
    // */
    //private String ebcCode;
    ///**
    // * 电商企业名称 电商企业的登记名称，对应清 单的收发货人。
    // */
    //private String ebcName;
    ///**
    // * 币制
    // * 海关标准的参数代码 《JGS-20 海关业务代码集》- 货币代码
    // * 142 人民币
    // */
    //private String currency = "142";
    ///**
    // * 电子订单类型:I进口
    // */
    //private String orderType = "I";
    ///**
    // * 担保企业编号
    // */
    //private String assureCode;
    ///**
    // * 物流企业代号
    // */
    //private String logisticsCode;
    //private String logisticsName;
    ///**
    // * 申报企业代码（关）
    // */
    //private String agentCodeCus;
    ///**
    // * 申报企业代码（检）
    // */
    //private String agentCodeCiq;
    ///**
    // * 贸易方式
    // */
    //private String tradeMode = "1210";
    //private String trafMode;
    ///**
    // * 运输工具编号
    // */
    //private String trafNo = "汽车";
    ///**
    // * 起运国   142中国
    // */
    //private String country = "142";
    ///**
    // * 币制（关）
    // */
    //private String currencyCus = "142";
    ///**
    // * 币制（检）
    // */
    //private String currencyCiq = "156";
    ///**
    // * 区内企业代码
    // */
    //private String areaCode;
    ///**
    // * 加密url
    // */
    //private String signUrl;
    //private String payCode;
    //private String payName;
    //private String payTransactionId;
    //
    ///**
    // * 查询域名
    // */
    //private String qeuryUrl;
    //
    ///**
    // * 311 查询
    // */
    //private String ceb312Uri = "ceb312msg?dxpid=DXPENT0000458763&qryid=";
    //
    ///**
    // * 622 查询
    // */
    //private String ceb622Uri = "ceb312msg?dxpid=DXPENT0000458763&qryid=";
    //
    ///**
    // * 口岸海关代码
    // *
    // * @apiNote 商品实际进出我国关境口岸海关 的关区代码，参照JGS/T 18《海关 关区代码》。
    // */
    //private String portCode;

}
