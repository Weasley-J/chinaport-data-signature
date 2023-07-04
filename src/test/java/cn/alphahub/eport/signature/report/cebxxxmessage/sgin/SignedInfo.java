package cn.alphahub.eport.signature.report.cebxxxmessage.sgin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static cn.alphahub.eport.signature.report.cebxxxmessage.entity.NameSpace.NAMESPACE_DS_URI;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SignedInfo", namespace = NAMESPACE_DS_URI)
public class SignedInfo {

    @XmlElement(name = "CanonicalizationMethod", namespace = NAMESPACE_DS_URI)
    private CanonicalizationMethod CanonicalizationMethod = new CanonicalizationMethod();

    @XmlElement(name = "SignatureMethod", namespace = NAMESPACE_DS_URI)
    private SignatureMethod SignatureMethod = new SignatureMethod();

    @XmlElement(name = "Reference", namespace = NAMESPACE_DS_URI)
    private Reference reference;
}
