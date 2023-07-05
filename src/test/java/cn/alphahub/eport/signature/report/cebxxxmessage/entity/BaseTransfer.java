package cn.alphahub.eport.signature.report.cebxxxmessage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static cn.alphahub.eport.signature.report.cebxxxmessage.entity.NameSpace.NAMESPACE_CEB_URI;

/**
 * 传输报文实体节点
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BaseTransfer", namespace = NAMESPACE_CEB_URI)
public class BaseTransfer implements Serializable {
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String copCode;

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String copName;

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String dxpMode;

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String dxpId;

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String note;
}
