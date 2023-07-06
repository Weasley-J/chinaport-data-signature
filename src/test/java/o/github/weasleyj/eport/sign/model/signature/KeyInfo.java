package o.github.weasleyj.eport.sign.model.signature;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static o.github.weasleyj.eport.sign.model.cebmessage.NameSpace.NAMESPACE_DS_URI;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "KeyInfo", namespace = NAMESPACE_DS_URI)
public class KeyInfo {
    @XmlElement(name = "KeyName", namespace = NAMESPACE_DS_URI)
    private String keyName;

    @XmlElement(name = "X509Data", namespace = NAMESPACE_DS_URI)
    private X509Data x509Data;
}
