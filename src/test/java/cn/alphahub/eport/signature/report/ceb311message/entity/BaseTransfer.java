package cn.alphahub.eport.signature.report.ceb311message.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

/**
 * 传输报文实体节点
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseTransfer implements Serializable {
    private String copCode;
    private String copName;
    private String dxpMode;
    private String dxpId;
    private String note;
}
