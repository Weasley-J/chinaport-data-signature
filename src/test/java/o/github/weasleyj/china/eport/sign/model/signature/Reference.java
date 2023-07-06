package o.github.weasleyj.china.eport.sign.model.signature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static o.github.weasleyj.china.eport.sign.model.cebmessage.NameSpace.NAMESPACE_DS_URI;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Reference", namespace = NAMESPACE_DS_URI)
public class Reference implements Serializable {

    @XmlAttribute(name = "URI")
    private String uri = "";

    @XmlElement(name = "Transforms", namespace = NAMESPACE_DS_URI)
    private Transforms transforms = new Transforms();

    @XmlElement(name = "DigestMethod", namespace = NAMESPACE_DS_URI)
    private DigestMethod digestMethod = new DigestMethod();

    @XmlElement(name = "DigestValue", namespace = NAMESPACE_DS_URI)
    private String digestValue;
}
