package cn.alphahub.eport.signature.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * XML Signature Algorithm
 *
 * @author weasley
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
public enum SignatureAlgorithm {
    RSA_SHA1("rsa-sha1", "http://www.w3.org/2000/09/xmldsig#rsa-sha1"),
    SM2_SM3("sm2-sm3", "http://www.w3.org/2000/09/xmldsig#sm2-sm3"),
    ;
    /**
     * 算法类型
     */
    private final String algorithmType;
    /**
     * XML代码段中s:SignatureMethod的算法值
     * <p>
     * XML Signature Method:
     * <pre>
     *  {@code <ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#${algorithmType}"/>}
     * </pre>
     */
    private final String xmlAlgorithmValue;
}
