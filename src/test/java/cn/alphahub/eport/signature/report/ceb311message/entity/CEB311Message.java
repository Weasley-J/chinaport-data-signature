package cn.alphahub.eport.signature.report.ceb311message.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static cn.alphahub.eport.signature.report.ceb311message.entity.NameSpace.NAMESPACE_CEB_URI;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CEB311Message", namespace = NAMESPACE_CEB_URI)
public class CEB311Message implements Serializable {
    @XmlElement(name = "Order", namespace = NAMESPACE_CEB_URI)
    private Order order;

    @XmlAttribute(name = "guid")
    private String guid;

    @XmlAttribute(name = "version")
    private String version;

    @XmlElement(name = "BaseSubscribe", namespace = NAMESPACE_CEB_URI)
    private BaseSubscribe baseSubscribe;

    @XmlElement(name = "BaseTransfer", namespace = NAMESPACE_CEB_URI)
    private BaseTransfer baseTransfer;

    @XmlElement(name = "ExtendMessage", namespace = NAMESPACE_CEB_URI)
    private ExtendMessage extendMessage;

}
