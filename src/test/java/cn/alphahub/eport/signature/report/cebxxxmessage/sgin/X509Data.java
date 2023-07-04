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
@XmlRootElement(name = "X509Data", namespace = NAMESPACE_DS_URI)
public class X509Data {

    @XmlElement(name = "X509Certificate", namespace = NAMESPACE_DS_URI)
    private String X509Certificate;
}
