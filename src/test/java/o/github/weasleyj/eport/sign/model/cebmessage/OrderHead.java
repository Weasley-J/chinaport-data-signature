package o.github.weasleyj.eport.sign.model.cebmessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static o.github.weasleyj.eport.sign.model.cebmessage.NameSpace.NAMESPACE_CEB_URI;

/**
 * OrderHead
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OrderHead ", namespace = NAMESPACE_CEB_URI)
public class OrderHead implements Serializable {
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String guid;//   1-系统唯一序号-guid-C36-是-企业系统生成36位唯一序号（英文字母大写）。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String appType;//      2-报送类型-appType-C1-是-企业报送类型。1-新增

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String appTime;// 3-报送时间-appTime-C14-是-企业报送时间。格式:YYYYMMDDhhmmss。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String appStatus;//         4-业务状态-appStatus-C..3-是-业务状态:1-暂存,2-申报,默认为2。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String orderType;//   5-订单类型-orderType-C1-是-电子订单类型：I进口

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String orderNo;// 6-订单编号-orderNo-C..60-是-交易平台的订单编号，同一交易平台的订单编号应唯一。订单编号长度不能超过60位。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ebpCode;//       7-电商平台代码-ebpCode-C..18-是-电商平台的海关注册登记编号；电商平台未在海关注册登记，由电商企业发送订单的，以中国电子口岸发布的电商平台标识编号为准。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ebpName;//     8-电商平台名称-ebpName-C..100-是-电商平台的海关注册登记名称；电商平台未在海关注册登记，由电商企业发送订单的，以中国电子口岸发布的电商平台名称为准。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ebcCode;//         9-电商企业代码-ebcCode-C..18-是-电商企业的海关注册登记编号。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ebcName;//        10-电商企业名称-ebcName-C..100-是-电商企业的海关注册登记名称。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String goodsValue;//         11-商品价格-goodsValue-N16,2-是-商品实际成交价，含非现金抵扣金额。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String freight;//       12-运杂费-freight-N16,2-是-不包含在商品价格中的运杂费，无则填写"0"。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String discount;//      13-非现金抵扣金额-discount-N16,2-是-使用积分、虚拟货币、代金券等非现金支付金额，无则填写"0"。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String taxTotal;//         14-代扣税款-taxTotal-N16,2-是-企业预先代扣的税款金额，无则填写“0”

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String acturalPaid;//        15-实际支付金额-acturalPaid-N16,2-是-商品价格+运杂费+代扣税款-非现金抵扣金额，与支付凭证的支付金额一致。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String currency;//       16-币制-currency-C3-是-限定为人民币，填写“142”。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String buyerRegNo;//        17-订购人注册号-buyerRegNo-C..60-是-订购人的交易平台注册号。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String buyerName;//        18-订购人姓名-buyerName-C..60-是-订购人的真实姓名。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String buyerIdType;//      19-订购人证件类型-buyerIdType-C1-是-1-身份证,2-其它。限定为身份证，填写“1”。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String buyerIdNumber;//       20-订购人证件号码-buyerIdNumber-C..60-是-订购人的身份证件号码。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String payCode;//     21-支付企业代码-payCode-C..18-否-支付企业的海关注册登记编号。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String payName;//      22-支付企业名称-payName-C..100-否-支付企业在海关注册登记的企业名称。

    @XmlElement(namespace = NAMESPACE_CEB_URI)

    private String payTransactionId;//      23-支付交易编号-payTransactionId-C..60-是-支付企业唯一的支付流水号。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String batchNumbers;//      24-商品批次号-batchNumbers-C..100-否-商品批次号。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String consignee;//      25-收货人姓名-consignee-C..100-是-收货人姓名，必须与电子运单的收货人姓名一致。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String consigneeTelephone;//      26-收货人电话-consigneeTelephone-C..50-是-收货人联系电话，必须与电子运单的收货人电话一致。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String consigneeAddress;//      27-收货地址-consigneeAddress-C..200-是-收货地址，必须与电子运单的收货地址一致。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String consigneeDitrict;//      28-收货地址行政区划代码-consigneeDitrict-C6-否-参照国家统计局公布的国家行政区划标准填制。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String note;//      29-备注-note-C..1000-否-

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String copNo;//     30-企业内部编号-copNo-C..20-是-企业内部标识单证的编号。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String assureCode;//      31-担保企业编号-assureCode-C..30-是-担保扣税的企业海关注册登记编号，只限清单的电商平台企业、电商企业、物流企业。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String buyerTelephone;//      32-订购人电话-buyerTelephone-C..30-是-订购人电话。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String country;//     33-起运国（地区）-country-C3-是-直购进口填写起始发出国家（地区）代码，参照《JGS-20 海关业务代码集》的国家（地区）代码表；保税进口填写代码“142”。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String netWeight;//     34-净重（公斤）-netWeight-N19,5-是-货物的毛重减去外包装材料后的重量，即货物本身的实际重量，计量单位为千克。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String agentCode;//     35-报关企业海关编码-agentCode-C..18-是-点上委托报关企业的海关编码

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String logisticsCode;// 36-物流企业代码-logisticsCode-C..18-是-物流企业的海关注册登记编号。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String logisticsNo;//    37-物流运单编号-logisticsNo-C..60-否-物流企业的运单包裹面单号。同一物流企业的运单编号在6个月内不重复。运单编号长度不能超过60位。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String isBuyAndPut;//     38-即买即提-isBuyAndPut-C1-是-Y：即买即提 N：不是即买即提

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String areaId;//  39-店铺海关编码-areaId-C..18-否-即买即提货物必须填写
}
