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
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SignedInfo", namespace = NameSpace.NAMESPACE_DS_URI)
public class SignedInfo implements Serializable {

    @XmlElement(name = "CanonicalizationMethod", namespace = NameSpace.NAMESPACE_DS_URI)
    private CanonicalizationMethod CanonicalizationMethod = new CanonicalizationMethod();

    @XmlElement(name = "SignatureMethod", namespace = NameSpace.NAMESPACE_DS_URI)
    private SignatureMethod SignatureMethod = new SignatureMethod();

    @XmlElement(name = "Reference", namespace = NameSpace.NAMESPACE_DS_URI)
    private Reference reference;
}
