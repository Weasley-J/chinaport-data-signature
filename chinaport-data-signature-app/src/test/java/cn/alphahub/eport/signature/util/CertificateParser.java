package cn.alphahub.eport.signature.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CertificateParser {
    public static void main(String[] args) {
        try {
            // 添加Bouncy Castle提供程序
            Security.addProvider(new BouncyCastleProvider());

            // 读取证书文件
            FileInputStream fis = new FileInputStream("/Users/weasley/Downloads/01691fe9.cer");
            //FileInputStream fis = new FileInputStream("/Users/weasley/Downloads/03000000000cde6f.cer");

            // 创建X.509证书工厂，指定使用Bouncy Castle提供程序
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");

            // 解析证书
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(fis);

            // 获取证书的基本信息
            System.out.println("序列号: " + certificate.getSerialNumber());
            System.out.println("签发者: " + certificate.getIssuerDN());
            System.out.println("主体: " + certificate.getSubjectDN());
            System.out.println("有效期起始日期: " + certificate.getNotBefore());
            System.out.println("有效期结束日期: " + certificate.getNotAfter());
            System.out.println("公钥算法: " + certificate.getPublicKey().getAlgorithm());
            System.out.println("签名算法: " + certificate.getSigAlgName());

            // 关闭文件输入流
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
