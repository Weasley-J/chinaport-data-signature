package cn.alphahub.eport.signature.core;

import cn.alphahub.eport.signature.config.SignatureAlgorithm;
import cn.hutool.core.date.LocalDateTimeUtil;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import static cn.alphahub.eport.signature.config.SignatureAlgorithm.RSA_SHA1;

/**
 * X509Certificate证书
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
public class CertificateHandler implements Serializable {
    /**
     * 从u-key读取含发送推送 X509Certificate 证书对应的加签方法
     */
    public static final String SING_DATA_METHOD = "cus-sec_SpcSignDataAsPEM";
    /**
     * 暂定2022-07-01为第一个时间分界
     */
    public static final LocalDateTime DATE_TIME_202207 = LocalDateTimeUtil.parse("2022-07-01", "yyyy-MM-dd");

    /**
     * u-key的.cer证书是否存在
     */
    private Boolean certExists = false;
    /**
     * XML 签名算法, RSA_SHA1 兜底
     */
    private SignatureAlgorithm algorithm = RSA_SHA1;
    /**
     * 判断用那一个证书的集合
     * <ul>
     *     <li>key   -> 从u-key读取<ds:SignatureValue>的方法名</li>
     *     <li>value -> 从u-key读取<ds:SignatureValue>的方法名对应的X509Certificate证书</li>
     * </ul>
     */
    @Setter
    @Getter
    private Map<String, String> x509Map;
    /**
     * 海关u-key证书有效期开始
     */
    private LocalDateTime ukeyValidTimeBegin;
    /**
     * 海关u-key证书有效期结束
     */
    private LocalDateTime ukeyValidTimeEnd;

    /**
     * 组装X509Certificate证书
     * <p>
     * 组装64位长度一行的pem分割的pem
     *
     * @param certPomFromUkey 从ukey里面读取的pem
     * @return 64位长度一行的pem分割的pem
     * @apiNote 不带Header
     */
    public static String buildX509CertificateWithoutHeader(String certPomFromUkey) {
        certPomFromUkey = certPomFromUkey.trim();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (char c : certPomFromUkey.toCharArray()) {
            sb.append(c);
            count++;
            if (count == 64) {
                sb.append("\n");
                count = 0;
            }
        }
        return sb.toString().trim();
    }

    /**
     * 组装X509Certificate证书
     * <p>
     * 组装64位长度一行的pem分割的pem
     *
     * @param certPomFromUkey 从ukey里面读取的pem
     * @return 64位长度一行的pem分割的pem
     * @apiNote 带Header
     */
    public static String buildX509CertificateWithHeader(String certPomFromUkey) {
        // TODO 待验证换行符，Windows 和其他平台有差异
        return "-----BEGIN CERTIFICATE-----\n"
                .concat(buildX509CertificateWithoutHeader(certPomFromUkey)).concat("\n")
                .concat("-----END CERTIFICATE-----");
    }

    public LocalDateTime getUkeyValidTimeBegin() {
        if (ukeyValidTimeBegin == null) {
            return LocalDateTime.now();
        }
        return ukeyValidTimeBegin;
    }

    /**
     * 获取509Certificate证书
     *
     * @param method 发送给电子口岸u-key的入参的[_method]字段的值
     * @return 509Certificate证书
     */
    public String getX509Certificate(String method) {
        return switch (method) {
            case SING_DATA_METHOD -> this.getX509Map().get(SING_DATA_METHOD);
            default -> throw new IllegalStateException("Unexpected value: " + method);
        };
    }

}

