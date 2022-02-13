package cn.alphahub.eport.signature.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Map;

/**
 * X509Certificate证书判断
 *
 * @author weasley
 * @version 1.0
 * @date 2022/2/13
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class X509CertificateHandler implements Serializable {
    /**
     * 从u-key读取含发送推送{@code <ds:X509Certificate>}(经HASH散列过)证书对应的方法
     */
    public static final String METHOD_OF_X509_WITH_HASH = "cus-sec_SpcSignDataAsPEM";
    /**
     * 从u-key读取含发送推送{@code <ds:X509Certificate>}(不经HASH散列，就是一个字符串)证书对应的方法
     */
    public static final String METHOD_OF_X509_WITHOUT_HASH = "cus-sec_SpcSignDataNoHashAsPEM";

    /**
     * 判断用那一个证书的集合
     * <ul>
     *     <li>key   -> 从u-key读取<ds:SignatureValue>的方法名</li>
     *     <li>value -> 从u-key读取<ds:SignatureValue>的方法名对应的X509Certificate证书</li>
     * </ul>
     */
    private Map<String, String> x509Map;

    /**
     * 获取509Certificate证书
     *
     * @param method 发送给电子口岸u-key的入参的[_method]字段的值
     * @return 509Certificate证书
     */
    public String get509Certificate(String method) {
        return switch (method) {
            case METHOD_OF_X509_WITH_HASH -> this.getX509Map().get(X509CertificateHandler.METHOD_OF_X509_WITH_HASH);
            case METHOD_OF_X509_WITHOUT_HASH -> this.getX509Map().get(X509CertificateHandler.METHOD_OF_X509_WITHOUT_HASH);
            default -> throw new IllegalStateException("Unexpected value: " + method);
        };
    }
}

