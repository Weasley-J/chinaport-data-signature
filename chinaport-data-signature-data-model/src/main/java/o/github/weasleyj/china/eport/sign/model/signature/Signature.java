package o.github.weasleyj.china.eport.sign.model.signature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static o.github.weasleyj.china.eport.sign.constants.NameSpace.NAMESPACE_DS_URI;

/**
 * Signature xml 节点
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Signature", namespace = NAMESPACE_DS_URI)
public class Signature implements Serializable {

    @XmlElement(name = "SignedInfo", namespace = NAMESPACE_DS_URI)
    private SignedInfo signedInfo;

    @XmlElement(name = "SignatureValue", namespace = NAMESPACE_DS_URI)
    private String signatureValue;

    @XmlElement(name = "KeyInfo", namespace = NAMESPACE_DS_URI)
    private KeyInfo keyInfo;
}
