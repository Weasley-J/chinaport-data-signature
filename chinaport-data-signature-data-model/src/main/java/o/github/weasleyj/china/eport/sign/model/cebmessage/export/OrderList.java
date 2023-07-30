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
 * The Order List
 * <p>订单商品表体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OrderList ", namespace = NAMESPACE_CEB_URI)
public class OrderList implements Serializable {
    /**
     * 1-商品序号-gnum-N2-是-从1开始的递增序号。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private Integer gnum;

    /**
     * 2-企业商品货号-itemNo-C..30-是-电商企业自定义的商品货号（SKU）。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String itemNo;

    /**
     * 3-企业商品名称-itemName-C..250-是-交易平台销售商品的中文名称。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String itemName;
    /**
     * 4-企业商品描述-itemDescribe-C..1000-否-交易平台销售商品的描述信息。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String itemDescribe;

    /**
     * 5-条形码-barCode-C..50-否-国际通用的商品条形码，一般由前缀部分、制造厂商代码、商品代码和校验码组成。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String barCode;
    /**
     * 6-单位-unit-C3-是-填写海关标准的参数代码，参照《JGS-20 海关业务代码集》- 计量单位代码。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String unit;
    /**
     * 10-币制-currency-C3-是-限定为人民币，填写“142”。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String currency;
    /**
     * 7-数量-qty-N16,2-是-商品实际数量。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String qty;

    /**
     * 8-单价-price-N16,2-是-商品单价。赠品单价填写为“0”。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String price;

    /**
     * 9-总价-totalPrice-N16,2-是-商品总价，等于单价乘以数量。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String totalPrice;

    /**
     * 12-备注-note-C..1000-否-促销活动，商品单价偏离市场价格的，可以在此说明。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String note;
}
