package cn.alphahub.eport.signature.core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
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
public class CertificateHandler implements Serializable {
    /**
     * 从u-key读取含发送推送{@code <ds:X509Certificate>}(经HASH散列过)证书对应的方法
     */
    public static final String METHOD_OF_X509_WITH_HASH = "cus-sec_SpcSignDataAsPEM";
    /**
     * 从u-key读取含发送推送{@code <ds:X509Certificate>}(不经HASH散列，就是一个字符串)证书对应的方法
     */
    public static final String METHOD_OF_X509_WITHOUT_HASH = "cus-sec_SpcSignDataNoHashAsPEM";

    /**
     * u-key的.cer证书是否存在
     */
    private Boolean certExists = false;
    /**
     * 判断用那一个证书的集合
     * <ul>
     *     <li>key   -> 从u-key读取<ds:SignatureValue>的方法名</li>
     *     <li>value -> 从u-key读取<ds:SignatureValue>的方法名对应的X509Certificate证书</li>
     * </ul>
     */
    private Map<String, String> x509Map;
    /**
     * 海关u-key证书有效期开始
     */
    private LocalDateTime ukeyValidTimeBegin;
    /**
     * 海关u-key证书有效期结束
     */
    private LocalDateTime ukeyValidTimeEnd;
    public CertificateHandler() {
    }

    public CertificateHandler(Map<String, String> x509Map) {
        this.x509Map = x509Map;
    }

    public LocalDateTime getUkeyValidTimeBegin() {
        if (ukeyValidTimeBegin == null) {
            return LocalDateTime.now();
        }
        return ukeyValidTimeBegin;
    }

    public Map<String, String> getX509Map() {
        return x509Map;
    }

    public void setX509Map(Map<String, String> x509Map) {
        this.x509Map = x509Map;
    }

    /**
     * 获取509Certificate证书
     *
     * @param method 发送给电子口岸u-key的入参的[_method]字段的值
     * @return 509Certificate证书
     */
    public String getX509Certificate(String method) {
        return switch (method) {
            case METHOD_OF_X509_WITH_HASH -> this.getX509Map().get(METHOD_OF_X509_WITH_HASH);
            case METHOD_OF_X509_WITHOUT_HASH -> this.getX509Map().get(METHOD_OF_X509_WITHOUT_HASH);
            default -> throw new IllegalStateException("Unexpected value: " + method);
        };
    }
}

