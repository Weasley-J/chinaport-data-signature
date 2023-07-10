package cn.alphahub.eport.signature.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * XML签名的算法类型配置类
 *
 * @author weasley
 */
@Data
@ConfigurationProperties(prefix = SignatureAlgorithmProperties.PREFIX)
public class SignatureAlgorithmProperties {
    public static final String PREFIX = "eport.signature";
    /**
     * XML签名的算法类型: RSA_SHA1, SM2_SM3
     *
     * @apiNote 配置文件里面大小写不敏感
     */
    private SignatureAlgorithm algorithm;
}
