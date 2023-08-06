package io.github.weasleyj.china.eport.sign.model.signature;

import io.github.weasleyj.china.eport.sign.constants.NameSpace;
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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Reference", namespace = NameSpace.NAMESPACE_DS_URI)
public class Reference implements Serializable {

    @XmlAttribute(name = "URI")
    private String uri = "";

    @XmlElement(name = "Transforms", namespace = NameSpace.NAMESPACE_DS_URI)
    private Transforms transforms = new Transforms();

    @XmlElement(name = "DigestMethod", namespace = NameSpace.NAMESPACE_DS_URI)
    private DigestMethod digestMethod = new DigestMethod();

    @XmlElement(name = "DigestValue", namespace = NameSpace.NAMESPACE_DS_URI)
    private String digestValue;
}
