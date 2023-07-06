package o.github.weasleyj.eport.signature.model.signxmlnode;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import static o.github.weasleyj.eport.signature.model.cebmessage.NameSpace.NAMESPACE_DS_URI;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DigestMethod", namespace = NAMESPACE_DS_URI)
public class DigestMethod {
    @XmlAttribute(name = "Algorithm")
    private String Algorithm = "http://www.w3.org/2000/09/xmldsig#sha1";
}
