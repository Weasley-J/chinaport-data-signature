package o.github.weasleyj.china.eport.sign.model.cebmessage.export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static o.github.weasleyj.china.eport.sign.constants.NameSpace.NAMESPACE_CEB_URI;

/**
 * 订单表头
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OrderHead ", namespace = NAMESPACE_CEB_URI)
public class OrderHead implements Serializable {
    /**
     * 1-系统唯一序号-guid-C36-是-企业系统生成36位唯一序号（英文字母大写）。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String guid;
    /**
     * 2-报送类型-appType-C1-是-企业报送类型。1-新增
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String appType;
    /**
     * 3-报送时间-appTime-C14-是-企业报送时间。格式:YYYYMMDDhhmmss。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String appTime;
    /**
     * 4-业务状态-appStatus-C..3-是-业务状态:1-暂存,2-申报,默认为2。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String appStatus;
    /**
     * 5-订单类型-orderType-C1-是-电子订单类型：I进口
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String orderType;
    /**
     * 6-订单编号-orderNo-C..60-是-交易平台的订单编号，同一交易平台的订单编号应唯一。订单编号长度不能超过60位。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String orderNo;
    /**
     * 7-电商平台代码-ebpCode-C..18-是-电商平台的海关注册登记编号；电商平台未在海关注册登记，由电商企业发送订单的，以中国电子口岸发布的电商平台标识编号为准。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ebpCode;
    /**
     * 8-电商平台名称-ebpName-C..100-是-电商平台的海关注册登记名称；电商平台未在海关注册登记，由电商企业发送订单的，以中国电子口岸发布的电商平台名称为准。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ebpName;
    /**
     * 9-电商企业代码-ebcCode-C..18-是-电商企业的海关注册登记编号。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ebcCode;
    /**
     * 10-电商企业名称-ebcName-C..100-是-电商企业的海关注册登记名称。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String ebcName;
    /**
     * 11-商品价格-goodsValue-N16,2-是-商品实际成交价，含非现金抵扣金额。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String goodsValue;
    /**
     * 12-运杂费-freight-N16,2-是-不包含在商品价格中的运杂费，无则填写"0"。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String freight;
    /**
     * 币制-海关标准的参数代码 《JGS-20 海关业务代码集》- 货币代码
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String currency;
    /**
     * 备注-note-C..1000-否-
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String note;
}
