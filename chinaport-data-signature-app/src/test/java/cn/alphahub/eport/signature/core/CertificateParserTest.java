package cn.alphahub.eport.signature.core;

import cn.alphahub.eport.signature.support.CertificateParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CertificateParserTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void parseCertificateByCertPath() {
        CertificateParser.parseCertificateByCertPath("/Users/weasley/Downloads/03000000000cde6f.cer");
        CertificateParser.parseCertificateByCertPath("/Users/weasley/Downloads/01691fe9.cer");
    }
}
