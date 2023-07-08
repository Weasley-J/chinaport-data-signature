package o.github.weasleyj.china.eport.sign.model.cebmessage;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static o.github.weasleyj.china.eport.sign.constants.NameSpace.NAMESPACE_CEB_URI;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OrderList ", namespace = NAMESPACE_CEB_URI)
public class OrderList {
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private Integer gnum;//  1-商品序号-gnum-N2-是-从1开始的递增序号。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String itemNo;//           2-企业商品货号-itemNo-C..30-是-电商企业自定义的商品货号（SKU）。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String itemName;//            3-企业商品名称-itemName-C..250-是-交易平台销售商品的中文名称。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String itemDescribe;//           4-企业商品描述-itemDescribe-C..1000-否-交易平台销售商品的描述信息。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String barCode;//           5-条形码-barCode-C..50-否-国际通用的商品条形码，一般由前缀部分、制造厂商代码、商品代码和校验码组成。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String unit;//           6-单位-unit-C3-是-填写海关标准的参数代码，参照《JGS-20 海关业务代码集》- 计量单位代码。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String qty;//           7-数量-qty-N16,2-是-商品实际数量。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String price;//           8-单价-price-N16,2-是-商品单价。赠品单价填写为“0”。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String totalPrice;//           9-总价-totalPrice-N16,2-是-商品总价，等于单价乘以数量。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String currency;//          10-币制-currency-C3-是-限定为人民币，填写“142”。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String country;//          11-原产国-country-C3-是-填写海关标准的参数代码，参照《JGS-20 海关业务代码集》-国家（地区）代码表。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String note;//           12-备注-note-C..1000-否-促销活动，商品单价偏离市场价格的，可以在此说明。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String gcode;//          13-商品编码-gcode-C10-是-按商品分类编码规则确定的进出口商品的商品编号，分为商品编号和附加编号，其中商品编号栏应填报《中华人民共和国进出口税则》8位税则号列，附加编号应填报商品编号，附加编号第9、10位。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String gmodel;//          14-商品规格型号-gmodel-C..250-是-满足海关归类、审价以及监管的要求为准。包括：品牌、规格、型号等。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String qty1;//          15-法定数量-qty1-N19,5-是-按照商品编码规则对应的法定计量单位的实际数量填写。

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String unit1;//           16-法定计量单位-unit1-C3-是-海关标准的参数代码 《JGS-20 海关业务代码集》- 计量单位代码

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String unit2;//       17-第二法定单位-unit2-C3-否-海关标准的参数代码 《JGS-20 海关业务代码集》- 计量单位代码

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String qty2;//  18-第二法定数量-qty2-N19,5-否-

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String gname;//            19-商品名称-gname-C..250-是-商品名称应据实填报，与电子订单一致。
}
