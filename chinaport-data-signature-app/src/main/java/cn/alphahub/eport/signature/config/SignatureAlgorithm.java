package cn.alphahub.eport.signature.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * XML Signature Algorithm
 *
 * @author weasley
 * @version 1.0.0
 * @apiNote 签名算法
 */
@Getter
@AllArgsConstructor
public enum SignatureAlgorithm {
    RSA_SHA1("rsa-sha1", "SHA1WITHRSA", "http://www.w3.org/2000/09/xmldsig#rsa-sha1"),
    SM2_SM3("sm2-sm3", "SM3WITHSM2", "http://www.w3.org/2000/09/xmldsig#sm2-sm3"),
    ;
    /**
     * 算法类型
     */
    private final String algorithmType;
    /**
     * 签名算法
     */
    private final String sigAlgName;
    /**
     * XML代码段中 ds:SignatureMethod 的算法值
     * <p>
     * XML Signature Method:
     * <pre>
     *  {@code <ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#${algorithmType}"/>}
     * </pre>
     */
    private final String signatureMethod;

    public static SignatureAlgorithm getSignatureAlgorithmSigAlgName(String sigAlgName) {
        return Arrays.stream(values())
                .filter(original -> Objects.equals(original.getSigAlgName(), sigAlgName))
                .findFirst().orElse(null);
    }
}
