package cn.alphahub.eport.signature.report.cebxxxmessage.sgin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import static cn.alphahub.eport.signature.report.cebxxxmessage.entity.NameSpace.NAMESPACE_DS_URI;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CanonicalizationMethod", namespace = NAMESPACE_DS_URI)
public class CanonicalizationMethod {

    @XmlAttribute(name = "Algorithm")
    private String Algorithm = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
}
