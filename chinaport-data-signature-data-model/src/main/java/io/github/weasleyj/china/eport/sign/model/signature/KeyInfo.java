package io.github.weasleyj.china.eport.sign.model.signature;

import io.github.weasleyj.china.eport.sign.constants.NameSpace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "KeyInfo", namespace = NameSpace.NAMESPACE_DS_URI)
public class KeyInfo {
    @XmlElement(name = "KeyName", namespace = NameSpace.NAMESPACE_DS_URI)
    private String keyName;

    @XmlElement(name = "X509Data", namespace = NameSpace.NAMESPACE_DS_URI)
    private X509Data x509Data;
}
