package cn.alphahub.eport.signature.report.cebxxxmessage.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static cn.alphahub.eport.signature.report.cebxxxmessage.entity.NameSpace.NAMESPACE_CEB_URI;


/**
 * 清关报文推送
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CEB621Message", namespace = NAMESPACE_CEB_URI)
public class CEB621Message {

    @XmlAttribute(name = "guid")
    private String guid;

    @XmlAttribute(name = "version")
    private String version;

    @XmlElement(name = "Inventory", namespace = NAMESPACE_CEB_URI)
    private Inventory inventory;

    @XmlElement(name = "BaseTransfer", namespace = NAMESPACE_CEB_URI)
    private BaseTransfer baseTransfer;
}
