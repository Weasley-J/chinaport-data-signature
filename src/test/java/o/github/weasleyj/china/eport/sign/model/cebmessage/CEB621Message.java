package o.github.weasleyj.china.eport.sign.model.cebmessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import o.github.weasleyj.china.eport.sign.AbstractCebMessage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


/**
 * CEB621Message 进口单清关报文
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CEB621Message", namespace = NameSpace.NAMESPACE_CEB_URI)
public class CEB621Message extends AbstractCebMessage implements Serializable {

    @XmlAttribute(name = "guid")
    private String guid;

    @XmlAttribute(name = "version")
    private String version;

    @XmlElement(name = "Inventory", namespace = NameSpace.NAMESPACE_CEB_URI)
    private Inventory inventory;

    @XmlElement(name = "BaseTransfer", namespace = NameSpace.NAMESPACE_CEB_URI)
    private BaseTransfer baseTransfer;
}
