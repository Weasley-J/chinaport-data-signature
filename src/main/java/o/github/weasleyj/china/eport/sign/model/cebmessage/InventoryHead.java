package o.github.weasleyj.china.eport.sign.model.cebmessage;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

import static o.github.weasleyj.china.eport.sign.constants.NameSpace.NAMESPACE_CEB_URI;

@Data
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InventoryHead ", namespace = NAMESPACE_CEB_URI)
public class InventoryHead {

    /**
     * 企业系统生成36位唯一序号 (英文字母大写)
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String guid;

    /**
     * 企业报送类型。1-新增 2-变 更 3-删除。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String appType;

    /**
     * 企业报送时间。 格式:YYYYMMDDhhmmss。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String appTime;

    /**
     * 企业报送状态。1-暂存,2-申 报。 填写2时,Signature节点必须 填写。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String appStatus;

    /**
     * 电商平台的交易订单编号，同 一平台的订单编号唯一不重 复
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String orderNo;

    /**
     * 电商平台代码 电商平台的海关注册登记编 号或统一社会信用代码。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ebpCode;

    /**
     * 电商平台名称 电商平台的登记名称。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ebpName;

    /**
     * 电商企业代码 电商企业的海关注册登记编
     * 号或统一社会信用代码，对应 清单的收发货人。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ebcCode;

    /**
     * 电商企业名称 电商企业的登记名称，对应清 单的收发货人。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ebcName;

    /**
     * 物流运单编号
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String logisticsNo;

    /**
     * 物流企业代码
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String logisticsCode;

    /**
     * 物流企业名称
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String logisticsName;

    /**
     * 企业内部编号
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String copNo;

    /**
     * 担保企业编号
     * 担保扣税的企业海关注册登记编 号，只限清单的电商平台企业、电 商企业、物流企业。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String assureCode;

    /**
     * 账册编号
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String emsNo;


    /**
     * 进出口标记 I-进口,E-出口
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ieFlag;

    /**
     * 申报日期 申报日期，以海关计算机系统接受 清单申报数据时记录的日期为准。 格式:YYYYMMDD。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String declTime;

    /**
     * 申报海关代码
     * 接受清单申报的海关关区代码，参 照JGS/T 18《海关关区代码》。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String customsCode;

    /**
     * 口岸海关代码
     * 商品实际进出我国关境口岸海关 的关区代码，参照JGS/T 18《海关 关区代码》。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String portCode;

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ieDate;

    /**
     * 订购人证件类 型 1-身份证,2-其它。限定为身份证， 填写“1”
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String buyerIdType;

    /**
     * 订购人证件号 码
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String buyerIdNumber;


    /**
     * 订购人姓名
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String buyerName;

    /**
     * 订购人电话
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String buyerTelephone;

    /**
     * 收件地址
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String consigneeAddress;

    /**
     * 申报企业代码
     * 申报单位的海关注册登记编号。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String agentCode;

    /**
     * 申报企业名称
     * 申报单位在海关注册登记的名称。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String agentName;

    /**
     * 区内企业编码
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String areaCode;

    /**
     * 区内企业名称
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String areaName;

    /**
     * 贸易方式
     * 直购进口填写“9610”，保税进口 填写“1210”。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String tradeMode;

    /**
     * 运输方式
     * 填写海关标准的参数代码，参照 《JGS-20 海关业务代码集》- 运 输方式代码。直购进口指跨境段物 流运输方式，保税进口指二线出区 物流运输方式。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String trafMode;

    /**
     * 起运国(地区)
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String country;

    /**
     * 运费
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private BigDecimal freight;

    /**
     * 保费
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private BigDecimal insuredFee;

    /**
     * 币制
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String currency;

    /**
     * 件数
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String packNo;

    /**
     * 毛重(公斤)
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private BigDecimal grossWeight;

    /**
     * 净重(公斤)
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private BigDecimal netWeight;

    /**
     * 备注
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String note;
}
