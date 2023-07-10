package cn.alphahub.eport.signature.core;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * X509 证书解析器
 *
 * @author weasley
 * @since 1.1.0
 */
public final class CertificateParser {
    private static final Logger log = LoggerFactory.getLogger(CertificateParser.class);

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 根据 'xxx.cer' 证书获取 X509 证书
     *
     * @param filePath 文件名， 如：/Users/weasley/Downloads/01691fe9.cer
     * @return X.509 certificate
     */
    @SuppressWarnings("all")
    public static X509Certificate parseCertificateByCertPath(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(fis);
            logCertificateInfo(certificate);
            return certificate;
        } catch (Exception e) {
            log.error("解析证书异常: {}", filePath, e);
            throw new RuntimeException("解析证书出错: " + filePath);
        }
    }

    /**
     * 根据 'xxx.cer' 证书获取 X509 证书
     *
     * @param certText xxx.cer文本内容, 包含证书头: -----BEGIN CERTIFICATE-----, -----END CERTIFICATE-----
     * @return X.509 certificate
     */
    @SuppressWarnings("all")
    public static X509Certificate parseCertificateByCertText(String certText) {
        try (InputStream is = new ByteArrayInputStream(certText.getBytes(StandardCharsets.UTF_8));) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(is);
            logCertificateInfo(certificate);
            return certificate;
        } catch (Exception e) {
            log.error("解析证书异常: {}", certText, e);
            throw new RuntimeException("解析证书出错: " + certText);
        }
    }

    /**
     * 证书信息
     *
     * @param certificate X509 证书
     */
    private static void logCertificateInfo(X509Certificate certificate) {
        if (log.isInfoEnabled()) {
            log.info("序列号: {}", certificate.getSerialNumber());
            log.info("签发者: {}", certificate.getIssuerX500Principal().getName());
            log.info("主体: {}", certificate.getSubjectX500Principal().getName());
            log.info("有效期起始日期: {}", certificate.getNotBefore());
            log.info("有效期结束日期: {}", certificate.getNotAfter());
            log.info("公钥算法: {}", certificate.getPublicKey().getAlgorithm());
            log.info("签名算法: {}", certificate.getSigAlgName());
        }
    }

}
