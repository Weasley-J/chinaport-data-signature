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
     * 本次加签是否成功
     */
    private Boolean success = false;
    /**
     * 签名的ukey的卡序列号
     */
    private String certNo;
    /**
     * 签名的ukey证书
     */
    private String x509Certificate;
    /**
     * XML报文的数字摘要
     */
    private String digestValue;
    /**
     * 调用ukey获取的签名值
     */
    private String signatureValue;
    /**
     * XML报文的签名节点
     * <li>用来组装 ds:SignedInfo 节点</li>
     */
    private String signatureNode;
}
