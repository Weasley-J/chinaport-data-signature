package o.github.weasleyj.china.eport.sign.model.cebmessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import static o.github.weasleyj.china.eport.sign.constants.NameSpace.NAMESPACE_CEB_URI;

/**
 * 传输报文实体节点
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BaseTransfer", namespace = NAMESPACE_CEB_URI)
public class BaseTransfer implements Serializable {
    /**
     * 传输企业代码-报文传输的企业代码(需要与接入客户端的企 业身份一致)
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String copCode;

    /**
     * 传输企业名称-报文传输的企业名称
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String copName;
    /**
     * 默认为 DXP; 指中国电子口岸数据交换平台
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String dxpMode;

    /**
     * 报文传输编号-向中国电子口岸数据中心申请数据交换平台 的用户编号
     */
    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String dxpId;

    @XmlElement(namespace = NAMESPACE_CEB_URI)
    private String note;

    /**
     * Build BaseTransfer XML Node
     *
     * @param copCode 传输企业代码-报文传输的企业代码(需要与接入客户端的企 业身份一致)
     * @param copName 传输企业名称-报文传输的企业名称
     * @param dxpId   报文传输编号-向中国电子口岸数据中心申请数据交换平台 的用户编号
     * @return Bean of BaseTransfer XML Node
     */
    public static BaseTransfer buildBaseTransfer(String copCode, String copName, String dxpId) {
        BaseTransfer baseTransfer = new BaseTransfer();
        baseTransfer.setCopCode(copCode);
        baseTransfer.setCopName(copName);
        baseTransfer.setDxpId(dxpId);
        baseTransfer.setDxpMode("DXP");
        return baseTransfer;
    }
}
