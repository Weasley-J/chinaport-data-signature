package io.github.weasleyj.china.eport.sign.model.signature;

import io.github.weasleyj.china.eport.sign.constants.NameSpace;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DigestMethod", namespace = NameSpace.NAMESPACE_DS_URI)
public class DigestMethod {
    @XmlAttribute(name = "Algorithm")
    private String Algorithm = "http://www.w3.org/2000/09/xmldsig#sha1";
}
