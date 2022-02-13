package cn.alphahub.eport.signature.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 加签结果返回
 *
 * @author weasley
 * @version 1.0
 * @date 2022/2/13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SignResult implements Serializable {
    /**
     * 本次是否加签成功
     */
    private Boolean success = false;
    /**
     * 签名的ukey的卡序列号,需要用专用的工具导出
     */
    private String certNo;
    /**
     * 签名的ukey证书,需要用专用工具导出
     */
    private String x509Certificate;
    /**
     * xml的数字摘要，先对不包含Signature节点的原始报文,进行C14n格式化，然后取shal二进制摘要，然后对sha1的值进行base64编码
     * <ul>
     *     <li><a href='http://tool.qdhuaxun.cn/ceb/CEB311Message.xml'> 待加签报文样例</a></li>
     * </ul>
     */
    private String digestValue;
    /**
     * 调用ukey获取的签名值
     */
    private String signatureValue;
}
