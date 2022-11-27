package cn.alphahub.eport.signature.config;

import cn.alphahub.eport.signature.entity.SignRequest;
import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
public class InitialConfigTest {

    /**
     * CEB621Message
     */
    public static String CEB621Message;

    static {
        try {
            File CEB621MessageFile = ResourceUtils.getFile("classpath:xml/CEB621Message.xml");
            CEB621Message = IoUtil.read(IoUtil.toStream(CEB621MessageFile), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getX509CertificateParameter() {
        System.err.println(InitialConfig.getX509CertificateParameter());
    }

    @Test
    void getDigestValueParameter() {
        System.err.println(InitialConfig.getDigestValueParameter(CEB621Message, 1));
    }

    @Test
    void getSignatureValueParameter() {
        System.err.println(InitialConfig.getSignDataAsPEMParameter(new SignRequest(CEB621Message)));
    }

    @Test
    void getVerifySignDataNoHashParameter() {
        String sha1DigestOfSourceXml = DigestUtil.sha1Hex(CEB621Message);
        System.err.println(sha1DigestOfSourceXml.length() + " " + sha1DigestOfSourceXml + " " + InitialConfig.getVerifySignDataNoHashParameter(CEB621Message, "sv", null, 1));
        sha1DigestOfSourceXml = org.apache.commons.codec.digest.DigestUtils.sha1Hex(CEB621Message);
        System.err.println(sha1DigestOfSourceXml.length() + " " + sha1DigestOfSourceXml + " " + InitialConfig.getVerifySignDataNoHashParameter(CEB621Message, "sv", null, 1));
    }

}
