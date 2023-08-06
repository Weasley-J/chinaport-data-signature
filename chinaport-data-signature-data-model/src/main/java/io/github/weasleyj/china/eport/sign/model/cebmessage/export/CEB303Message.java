package io.github.weasleyj.china.eport.sign.model.cebmessage.export;

import io.github.weasleyj.china.eport.sign.AbstractCebMessage;
import io.github.weasleyj.china.eport.sign.constants.NameSpace;
import io.github.weasleyj.china.eport.sign.model.cebmessage.BaseSubscribe;
import io.github.weasleyj.china.eport.sign.model.cebmessage.BaseTransfer;
import io.github.weasleyj.china.eport.sign.model.cebmessage.ExtendMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * CEB303Message 出口单
 *
 * @author weasley
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CEB303Message", namespace = NameSpace.NAMESPACE_CEB_URI)
public class CEB303Message extends AbstractCebMessage implements Serializable {
    @XmlAttribute(name = "guid")
    private String guid;

    @XmlAttribute(name = "version")
    private String version;

    @XmlElement(name = "Order", namespace = NameSpace.NAMESPACE_CEB_URI)
    private Order order;

    @XmlElement(name = "BaseTransfer", namespace = NameSpace.NAMESPACE_CEB_URI)
    private BaseTransfer baseTransfer;

    @XmlElement(name = "BaseSubscribe", namespace = NameSpace.NAMESPACE_CEB_URI)
    private BaseSubscribe baseSubscribe;

    @XmlElement(name = "ExtendMessage", namespace = NameSpace.NAMESPACE_CEB_URI)
    private ExtendMessage extendMessage;
}
