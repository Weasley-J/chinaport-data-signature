package io.github.weasleyj.china.eport.sign.model.cebmessage;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

import static io.github.weasleyj.china.eport.sign.constants.NameSpace.NAMESPACE_CEB_URI;

@Data
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "InventoryList ", namespace = NAMESPACE_CEB_URI)
public class InventoryList implements Serializable {
    /**
     * 序号
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private Integer gnum;

    /**
     * 账册备案料号
     * 保税进口必填
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String itemRecordNo;

    /**
     * 企业商品货号
     * 电商企业自定义的商品货号 (SKU)。
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String itemNo;

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String itemName;

    /**
     * 商品编码
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String gcode;

    /**
     * 商品名称
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String gname;

    /**
     * 商品规格型号
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String gmodel;

    /**
     * 条形码
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String barCode;

    /**
     * 原产国(地区)
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String country;

    /**
     * 币制
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String currency;

    /**
     * 数量
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private BigDecimal qty;
    /**
     * 计量单位
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String unit;
    /**
     * 法定数量
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private BigDecimal qty1;

    /**
     * 法定计量单位
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String unit1;

    /**
     * 法定第二计量单位
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String unit2;

    /**
     * 第二数量
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private BigDecimal qty2;

    /**
     * 单价
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private BigDecimal price;

    /**
     * 总价
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private BigDecimal totalPrice;

    /**
     * 备注
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String note;

}
