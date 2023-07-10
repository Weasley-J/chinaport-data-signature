package cn.alphahub.eport.signature.client;

import cn.alphahub.dtt.plus.util.SpringUtil;
import cn.alphahub.eport.signature.config.UkeyInitialConfig;
import cn.alphahub.eport.signature.config.UkeyInitialConfigTest;
import cn.alphahub.eport.signature.core.CertificateHandler;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.alphahub.eport.signature.entity.UkeyResponse.Args;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * WebSocketClient测试类
 *
 * @author weasley
 * @version 1.0
 * @date 2022/2/12
 */
@Slf4j
@SpringBootTest
class SignClientTest {
    private static final String PASSWORD = "88888888";

    @Autowired
    private SignHandler signClient;

    @Test
    void sendInitDataAsPEMWithHashPTest() {
        SignRequest request = new SignRequest(UkeyInitialConfigTest.CEB621Message);
        String parameter = UkeyInitialConfig.getSignDataAsPEM(request);
        SignResult result = signClient.sign(request, parameter);
        System.out.println(JSONUtil.toJsonPrettyStr(result));
    }

    @Test
    void sendInitDataNoHashAsPEMParameterTest() {
        SignRequest request = new SignRequest(UkeyInitialConfigTest.CEB621Message);
        String parameter = UkeyInitialConfig.getSignDataNoHashAsPEMP(request);
        SignResult result = signClient.sign(request, parameter);
        System.out.println(JSONUtil.toJsonPrettyStr(result));
    }

    /**
     * 获取版本号
     */
    @Test
    void ukeyVersion() {
        String method = "cus-sec_SpcGetCardAttachInfo";
        UkeyRequest ukeyRequest = new UkeyRequest(method, new LinkedHashMap<>() {{
            put("passwd", PASSWORD);
        }});
        Args args = this.signClient.getUkeyResponseArgs(ukeyRequest);
        System.out.println(JSONUtil.toJsonPrettyStr(args));
    }

    /**
     * 使用卡计算摘要,返回PEM格式信息
     */
    @Test
    @DisplayName("使用卡计算摘要,返回PEM格式信息")
    void calculationSummaryUsingCards() {
        String method = "cus-sec_SpcSHA1Digest";
        UkeyRequest ukeyRequest = new UkeyRequest(method, new LinkedHashMap<>() {{
            put("szInfo", """
                    <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#"><ds:SignedInfo><ds:CanonicalizationMethod Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"/><ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sm2-sm3"/><ds:Reference URI=""><ds:Transforms><ds:Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/></ds:Transforms><ds:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/><ds:DigestValue>8JmrLw4jIIsHx+qb7D7U9eKPlWo=</ds:DigestValue></ds:Reference></ds:SignedInfo>
                    """);
            put("passwd", PASSWORD);
        }});
        Args args = this.signClient.getUkeyResponseArgs(ukeyRequest);
        System.out.println(JSONUtil.toJsonPrettyStr(args));
    }

    @Test
    void calculationSummaryUsingCards2() {
        String method = "cus-sec_SpcSHA1DigestAsPEM";
        String data = """
                <?xml version="1.0" encoding="UTF-8"?>
                <ceb:CEB311Message guid="tCEB311_HNZB_HNFX_20230707223752_003" version="1.0" xmlns:ceb="http://www.chinaport.gov.cn/ceb" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                    <ceb:Order>
                        <ceb:OrderHead>
                            <ceb:guid>tCEB311_HNZB_HNFX_20230707223752_001</ceb:guid>
                            <ceb:appType>1</ceb:appType>
                            <ceb:appTime>20230704181028</ceb:appTime>
                            <ceb:appStatus>2</ceb:appStatus>
                            <ceb:orderType>I</ceb:orderType>
                            <ceb:orderNo>T_C5051511332138160010</ceb:orderNo>
                            <ceb:ebpCode>4601630004</ceb:ebpCode>
                            <ceb:ebpName>海南省荣誉进出口贸易有限公司</ceb:ebpName>
                            <ceb:ebcCode>4601630004</ceb:ebcCode>
                            <ceb:ebcName>海南省荣誉进出口贸易有限公司</ceb:ebcName>
                            <ceb:goodsValue>0.01</ceb:goodsValue>
                            <ceb:freight>0</ceb:freight>
                            <ceb:discount>0</ceb:discount>
                            <ceb:taxTotal>0</ceb:taxTotal>
                            <ceb:acturalPaid>0.01</ceb:acturalPaid>
                            <ceb:currency>142</ceb:currency>
                            <ceb:buyerRegNo>4</ceb:buyerRegNo>
                            <ceb:buyerName>袁晓雨</ceb:buyerName>
                            <ceb:buyerTelephone>13701727375</ceb:buyerTelephone>
                            <ceb:buyerIdType>1</ceb:buyerIdType>
                            <ceb:buyerIdNumber>130435200009241538</ceb:buyerIdNumber>
                            <ceb:consignee>袁晓雨</ceb:consignee>
                            <ceb:consigneeTelephone>13701727375</ceb:consigneeTelephone>
                            <ceb:consigneeAddress>北京北京市东城区</ceb:consigneeAddress>
                            <ceb:note>test</ceb:note>
                        </ceb:OrderHead>
                        <ceb:OrderList>
                            <ceb:gnum>1</ceb:gnum>
                            <ceb:itemNo>1</ceb:itemNo>
                            <ceb:itemName>LANNA兰纳</ceb:itemName>
                            <ceb:gmodel>10片/包</ceb:gmodel>
                            <ceb:itemDescribe></ceb:itemDescribe>
                            <ceb:barCode>1</ceb:barCode>
                            <ceb:unit>011</ceb:unit>
                            <ceb:qty>1</ceb:qty>
                            <ceb:price>1</ceb:price>
                            <ceb:totalPrice>1</ceb:totalPrice>
                            <ceb:currency>142</ceb:currency>
                            <ceb:country>136</ceb:country>
                            <ceb:note>test</ceb:note>
                        </ceb:OrderList>
                    </ceb:Order>
                    <ceb:BaseTransfer>
                        <ceb:copCode>4601630004</ceb:copCode>
                        <ceb:copName>海南省荣誉进出口贸易有限公司</ceb:copName>
                        <ceb:dxpMode>DXP</ceb:dxpMode>
                        <ceb:dxpId>DXPENT0000530815</ceb:dxpId>
                        <ceb:note>test</ceb:note>
                    </ceb:BaseTransfer>
                </ceb:CEB311Message>
                """;
        SignRequest signRequest = new SignRequest(data);
        UkeyRequest ukeyRequest = JSONUtil.toBean(UkeyInitialConfig.getSHA1DigestAsPEMParams(signRequest), new TypeReference<UkeyRequest>() {
        }, true);
        Args args = this.signClient.getUkeyResponseArgs(ukeyRequest);
        System.out.println(JSONUtil.toJsonPrettyStr(args));
    }

    @Test
    @DisplayName("测试验签结果")
    void verifySignDataNoHash() {
        String parameter = UkeyInitialConfig.getVerifySignDataNoHashParameter(
                """
                        <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#"><ds:SignedInfo><ds:CanonicalizationMethod Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"/><ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sm2-sm3"/><ds:Reference URI=""><ds:Transforms><ds:Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/></ds:Transforms><ds:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/><ds:DigestValue>8JmrLw4jIIsHx+qb7D7U9eKPlWo=</ds:DigestValue></ds:Reference></ds:SignedInfo>
                        """,
                """
                        fWRjWJclZkGi1nnW/VzIKo8oy6exjY5TSTTyKulYm/9cp/NeYnu33VA4C751nH2lOyZN3YcjIp2SO1E80LVAgg==
                        """,
                null, 1);
        UkeyRequest ukeyRequest = JSONUtil.toBean(parameter, new TypeReference<>() {
        }, true);
        Args args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.out.println(JSONUtil.toJsonPrettyStr(args));
    }

    @Test
    @DisplayName("获取Usb-Key的序列号(ID)")
    void t1() {
        HashMap<String, Object> _args = new HashMap<>();
        UkeyRequest ukeyRequest = new UkeyRequest("cus-sec_SpcGetCardID", _args);
        Args args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("获取卡号: " + JSONUtil.toJsonStr(args));

        ukeyRequest = new UkeyRequest("cus-cus-sec_SpcGetCardAttachInfo", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("获取附加信息: " + JSONUtil.toJsonStr(args));

        ukeyRequest = new UkeyRequest("cus-sec_SpcGetCertNo", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("获取证书编号: " + JSONUtil.toJsonStr(args));

        ukeyRequest = new UkeyRequest("cus-sec_SpcGetUName", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("申请者名称: " + JSONUtil.toJsonStr(args));

        ukeyRequest = new UkeyRequest("cus-sec_SpcGetEntID", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("单位ID: " + JSONUtil.toJsonStr(args));

        ukeyRequest = new UkeyRequest("cus-sec_SpcGetEntName", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("单位名: " + JSONUtil.toJsonStr(args));

        ukeyRequest = new UkeyRequest("cus-sec_SpcGetSignCert", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("取海关签名证书: " + JSONUtil.toJsonStr(args));

        ukeyRequest = new UkeyRequest("cus-sec_SpcGetSignCertAsPEM", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("取海关签名证书PEM: " + JSONUtil.toJsonStr(args));

        ukeyRequest = new UkeyRequest("cus-sec_SpcGetEnvCert", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("取海关加密证书: " + JSONUtil.toJsonStr(args));

        ukeyRequest = new UkeyRequest("cus-sec_SpcGetEnvCertAsPEM", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("取海关加密证书PEM: " + JSONUtil.toJsonStr(args));

        ukeyRequest = new UkeyRequest("cus-sec_SpcGetValidTimeFromCert", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("查看海关证书有效期: " + JSONUtil.toJsonStr(args));

        // inData 原始内容
        _args.put("inData", "MIIEoDCCBESgAwIBAgIIAwAAAAAM3m8wDAYIKoEcz1UBg3UFADCBmDELMAkGA1UEBhMCQ04xDzANBgNVBAgMBuWMl+S6rDEPMA0GA1UEBwwG5YyX5LqsMRswGQYDVQQKDBLkuK3lm73nlLXlrZDlj6PlsrgxGzAZBgNVBAsMEuivgeS5pueuoeeQhuS4reW/gzEtMCsGA1UEAwwk5Lit5Zu955S15a2Q5Lia5Yqh6K+B5Lmm566h55CG5Lit5b+DMB4XDTIzMDMyOTAwMDAwMFoXDTMzMDMyOTAwMDAwMFowVjELMAkGA1UEBhMCQ04xMzAxBgNVBAsMKua1t+WNl+ecgeiNo+iqiei/m+WHuuWPo+i0uOaYk+aciemZkOWFrOWPuDESMBAGA1UEAwwJ5p2o5aaC6YeRMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE0vOQmplAr9igPZrA8F1msqnFd0U++6G6NhG5rNuIUWft0BwQn7eSJkt5/fvSSoe7pUg2/awHUWPnzkeeQc7oVqOCArUwggKxMBEGCWCGSAGG+EIBAQQEAwIFoDAOBgNVHQ8BAf8EBAMCBsAwCQYDVR0TBAIwADApBgNVHSUEIjAgBggrBgEFBQcDAgYIKwYBBQUHAwQGCisGAQQBgjcUAgIwHwYDVR0jBBgwFoAURCQxt0wEvoAVXmuo4N1bjKXTh0UwHQYDVR0OBBYEFAytGob5L0WqhOCZ5l6Lf2jUdNrAMGgGA1UdIARhMF8wXQYEVR0gADBVMFMGCCsGAQUFBwIBFkdodHRwczovL3d3dy5jaGluYXBvcnQuZ292LmNuL3RjbXNmaWxlL3UvY21zL3d3dy8yMDIyMDQvMTIxMzI5NDh4dDZwLnBkZjB/BgNVHR8EeDB2MHSgcqBwhm5sZGFwOi8vbGRhcC5jaGluYXBvcnQuZ292LmNuOjM4OS9jbj1jcmwwMzAwMDAsb3U9Y3JsMDAsb3U9Y3JsLGM9Y24/Y2VydGlmaWNhdGVSZXZvY2F0aW9uTGlzdD9iYXNlP2NuPWNybDAzMDAwMDA+BggrBgEFBQcBAQQyMDAwLgYIKwYBBQUHMAGGImh0dHA6Ly9vY3NwLmNoaW5hcG9ydC5nb3YuY246ODgwMC8wOgYKKwYBBAGpQ2QFAQQsDCrmtbfljZfnnIHojaPoqonov5vlh7rlj6PotLjmmJPmnInpmZDlhazlj7gwEgYKKwYBBAGpQ2QFAwQEDAIwMTAiBgorBgEEAalDZAUIBBQMEjUxMjMyNDE5NjQxMDE3Mjk3WDAgBgorBgEEAalDZAUJBBIMEDAzLUpKMEc5MDAyMjA3NTIwGQYKKwYBBAGpQ2QFCwQLDAlNQTVUTkZHWTkwEgYKKwYBBAGpQ2QFDAQEDAIwMDASBgorBgEEAalDZAIBBAQMAjEyMBIGCisGAQQBqUNkAgQEBAwCMTQwDAYIKoEcz1UBg3UFAANIADBFAiBM4OVAc8aaCZU4XFfcVMkC7bWIIenRnPLxrnwVeYO3CQIhANQ767YIurkJCoLtwyqQPbUZe/+3BjGZcIWqB1mAl9T+"); //原始内容
        ukeyRequest = new UkeyRequest("cus-sec_SpcEncodePEM", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("PEM编码: " + JSONUtil.toJsonStr(args));

        //inData PEM编码的内容(字符串)
        _args.put("inData", "MIIEoDCCBESgAwIBAgIIAwAAAAAM3m8wDAYIKoEcz1UBg3UFADCBmDELMAkGA1UEBhMCQ04xDzANBgNVBAgMBuWMl+S6rDEPMA0GA1UEBwwG5YyX5LqsMRswGQYDVQQKDBLkuK3lm73nlLXlrZDlj6PlsrgxGzAZBgNVBAsMEuivgeS5pueuoeeQhuS4reW/gzEtMCsGA1UEAwwk5Lit5Zu955S15a2Q5Lia5Yqh6K+B5Lmm566h55CG5Lit5b+DMB4XDTIzMDMyOTAwMDAwMFoXDTMzMDMyOTAwMDAwMFowVjELMAkGA1UEBhMCQ04xMzAxBgNVBAsMKua1t+WNl+ecgeiNo+iqiei/m+WHuuWPo+i0uOaYk+aciemZkOWFrOWPuDESMBAGA1UEAwwJ5p2o5aaC6YeRMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE0vOQmplAr9igPZrA8F1msqnFd0U++6G6NhG5rNuIUWft0BwQn7eSJkt5/fvSSoe7pUg2/awHUWPnzkeeQc7oVqOCArUwggKxMBEGCWCGSAGG+EIBAQQEAwIFoDAOBgNVHQ8BAf8EBAMCBsAwCQYDVR0TBAIwADApBgNVHSUEIjAgBggrBgEFBQcDAgYIKwYBBQUHAwQGCisGAQQBgjcUAgIwHwYDVR0jBBgwFoAURCQxt0wEvoAVXmuo4N1bjKXTh0UwHQYDVR0OBBYEFAytGob5L0WqhOCZ5l6Lf2jUdNrAMGgGA1UdIARhMF8wXQYEVR0gADBVMFMGCCsGAQUFBwIBFkdodHRwczovL3d3dy5jaGluYXBvcnQuZ292LmNuL3RjbXNmaWxlL3UvY21zL3d3dy8yMDIyMDQvMTIxMzI5NDh4dDZwLnBkZjB/BgNVHR8EeDB2MHSgcqBwhm5sZGFwOi8vbGRhcC5jaGluYXBvcnQuZ292LmNuOjM4OS9jbj1jcmwwMzAwMDAsb3U9Y3JsMDAsb3U9Y3JsLGM9Y24/Y2VydGlmaWNhdGVSZXZvY2F0aW9uTGlzdD9iYXNlP2NuPWNybDAzMDAwMDA+BggrBgEFBQcBAQQyMDAwLgYIKwYBBQUHMAGGImh0dHA6Ly9vY3NwLmNoaW5hcG9ydC5nb3YuY246ODgwMC8wOgYKKwYBBAGpQ2QFAQQsDCrmtbfljZfnnIHojaPoqonov5vlh7rlj6PotLjmmJPmnInpmZDlhazlj7gwEgYKKwYBBAGpQ2QFAwQEDAIwMTAiBgorBgEEAalDZAUIBBQMEjUxMjMyNDE5NjQxMDE3Mjk3WDAgBgorBgEEAalDZAUJBBIMEDAzLUpKMEc5MDAyMjA3NTIwGQYKKwYBBAGpQ2QFCwQLDAlNQTVUTkZHWTkwEgYKKwYBBAGpQ2QFDAQEDAIwMDASBgorBgEEAalDZAIBBAQMAjEyMBIGCisGAQQBqUNkAgQEBAwCMTQwDAYIKoEcz1UBg3UFAANIADBFAiBM4OVAc8aaCZU4XFfcVMkC7bWIIenRnPLxrnwVeYO3CQIhANQ767YIurkJCoLtwyqQPbUZe/+3BjGZcIWqB1mAl9T+"); //原始内容
        ukeyRequest = new UkeyRequest("cus-sec_SpcDecodePEM", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("PEM编码: " + JSONUtil.toJsonStr(args));

        // inData PEM编码的内容(字符串), PEM解码,返回String类型的原文
        _args.put("inData", "MIIEoDCCBESgAwIBAgIIAwAAAAAM3m8wDAYIKoEcz1UBg3UFADCBmDELMAkGA1UEBhMCQ04xDzANBgNVBAgMBuWMl+S6rDEPMA0GA1UEBwwG5YyX5LqsMRswGQYDVQQKDBLkuK3lm73nlLXlrZDlj6PlsrgxGzAZBgNVBAsMEuivgeS5pueuoeeQhuS4reW/gzEtMCsGA1UEAwwk5Lit5Zu955S15a2Q5Lia5Yqh6K+B5Lmm566h55CG5Lit5b+DMB4XDTIzMDMyOTAwMDAwMFoXDTMzMDMyOTAwMDAwMFowVjELMAkGA1UEBhMCQ04xMzAxBgNVBAsMKua1t+WNl+ecgeiNo+iqiei/m+WHuuWPo+i0uOaYk+aciemZkOWFrOWPuDESMBAGA1UEAwwJ5p2o5aaC6YeRMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE0vOQmplAr9igPZrA8F1msqnFd0U++6G6NhG5rNuIUWft0BwQn7eSJkt5/fvSSoe7pUg2/awHUWPnzkeeQc7oVqOCArUwggKxMBEGCWCGSAGG+EIBAQQEAwIFoDAOBgNVHQ8BAf8EBAMCBsAwCQYDVR0TBAIwADApBgNVHSUEIjAgBggrBgEFBQcDAgYIKwYBBQUHAwQGCisGAQQBgjcUAgIwHwYDVR0jBBgwFoAURCQxt0wEvoAVXmuo4N1bjKXTh0UwHQYDVR0OBBYEFAytGob5L0WqhOCZ5l6Lf2jUdNrAMGgGA1UdIARhMF8wXQYEVR0gADBVMFMGCCsGAQUFBwIBFkdodHRwczovL3d3dy5jaGluYXBvcnQuZ292LmNuL3RjbXNmaWxlL3UvY21zL3d3dy8yMDIyMDQvMTIxMzI5NDh4dDZwLnBkZjB/BgNVHR8EeDB2MHSgcqBwhm5sZGFwOi8vbGRhcC5jaGluYXBvcnQuZ292LmNuOjM4OS9jbj1jcmwwMzAwMDAsb3U9Y3JsMDAsb3U9Y3JsLGM9Y24/Y2VydGlmaWNhdGVSZXZvY2F0aW9uTGlzdD9iYXNlP2NuPWNybDAzMDAwMDA+BggrBgEFBQcBAQQyMDAwLgYIKwYBBQUHMAGGImh0dHA6Ly9vY3NwLmNoaW5hcG9ydC5nb3YuY246ODgwMC8wOgYKKwYBBAGpQ2QFAQQsDCrmtbfljZfnnIHojaPoqonov5vlh7rlj6PotLjmmJPmnInpmZDlhazlj7gwEgYKKwYBBAGpQ2QFAwQEDAIwMTAiBgorBgEEAalDZAUIBBQMEjUxMjMyNDE5NjQxMDE3Mjk3WDAgBgorBgEEAalDZAUJBBIMEDAzLUpKMEc5MDAyMjA3NTIwGQYKKwYBBAGpQ2QFCwQLDAlNQTVUTkZHWTkwEgYKKwYBBAGpQ2QFDAQEDAIwMDASBgorBgEEAalDZAIBBAQMAjEyMBIGCisGAQQBqUNkAgQEBAwCMTQwDAYIKoEcz1UBg3UFAANIADBFAiBM4OVAc8aaCZU4XFfcVMkC7bWIIenRnPLxrnwVeYO3CQIhANQ767YIurkJCoLtwyqQPbUZe/+3BjGZcIWqB1mAl9T+"); //原始内容
        ukeyRequest = new UkeyRequest("cus-sec_SpcDecodePEMAsString", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("PEM解码,返回String类型的原文: " + JSONUtil.toJsonStr(args));
    }


    @Test
    @DisplayName("签名+取海关签名证书PEM+验证签名")
    void t2() {
        HashMap<String, Object> _args = new HashMap<>();
        String xml = """
                <ds:SignedInfo xmlns:ceb="http://www.chinaport.gov.cn/ceb" xmlns:ds="http://www.w3.org/2000/09/xmldsig#" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><ds:CanonicalizationMethod Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"></ds:CanonicalizationMethod><ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sm2-sm3"></ds:SignatureMethod><ds:Reference URI=""><ds:Transforms><ds:Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"></ds:DigestMethod><ds:DigestValue>r1t2B+VZLNVPtNJXAw9gcf3gGKU=</ds:DigestValue></ds:Reference></ds:SignedInfo>
                """;
        _args.put("inData", xml);
        _args.put("passwd", PASSWORD);
        UkeyRequest ukeyRequest = new UkeyRequest("cus-sec_SpcSignDataAsPEM", _args);
        Args args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("签名,返回PEM格式: " + args.getData().get(0));

        ukeyRequest = new UkeyRequest("cus-sec_SpcGetSignCertAsPEM", _args);
        Args args2 = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("取海关签名证书PEM: " + JSONUtil.toJsonStr(args2));

        CertificateHandler reportClient = SpringUtil.getBean(CertificateHandler.class);
        _args.put("inData", xml); //原文信息
        _args.put("signData", args.getData().get(0)); //签名信息
        //海关签名证书PEM
        //_args.put("certDataPEM", reportClient.buildX509Certificate(args2.getData().get(0)).replaceAll("\n",""));
        _args.put("certDataPEM", args2.getData().get(0));
        args = signClient.getUkeyResponseArgs(new UkeyRequest("cus-sec_SpcVerifySignData", _args));
        System.err.println("验证签名: " + args.getData().get(0));
        //签名,返回PEM格式: PABavspV+F57kSl3bzcPmqqWg1fEq6e+TrdwuKr3jDGEtpTuxuBdY2IB+aTGyLFfagQZ6fo8Ph4541OCe6rhPQ==
        //取海关签名证书PEM: {"Result":true,"Data":["MIIEoDCCBESgAwIBAgIIAwAAAAAM3m8wDAYIKoEcz1UBg3UFADCBmDELMAkGA1UEBhMCQ04xDzANBgNVBAgMBuWMl+S6rDEPMA0GA1UEBwwG5YyX5LqsMRswGQYDVQQKDBLkuK3lm73nlLXlrZDlj6PlsrgxGzAZBgNVBAsMEuivgeS5pueuoeeQhuS4reW/gzEtMCsGA1UEAwwk5Lit5Zu955S15a2Q5Lia5Yqh6K+B5Lmm566h55CG5Lit5b+DMB4XDTIzMDMyOTAwMDAwMFoXDTMzMDMyOTAwMDAwMFowVjELMAkGA1UEBhMCQ04xMzAxBgNVBAsMKua1t+WNl+ecgeiNo+iqiei/m+WHuuWPo+i0uOaYk+aciemZkOWFrOWPuDESMBAGA1UEAwwJ5p2o5aaC6YeRMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE0vOQmplAr9igPZrA8F1msqnFd0U++6G6NhG5rNuIUWft0BwQn7eSJkt5/fvSSoe7pUg2/awHUWPnzkeeQc7oVqOCArUwggKxMBEGCWCGSAGG+EIBAQQEAwIFoDAOBgNVHQ8BAf8EBAMCBsAwCQYDVR0TBAIwADApBgNVHSUEIjAgBggrBgEFBQcDAgYIKwYBBQUHAwQGCisGAQQBgjcUAgIwHwYDVR0jBBgwFoAURCQxt0wEvoAVXmuo4N1bjKXTh0UwHQYDVR0OBBYEFAytGob5L0WqhOCZ5l6Lf2jUdNrAMGgGA1UdIARhMF8wXQYEVR0gADBVMFMGCCsGAQUFBwIBFkdodHRwczovL3d3dy5jaGluYXBvcnQuZ292LmNuL3RjbXNmaWxlL3UvY21zL3d3dy8yMDIyMDQvMTIxMzI5NDh4dDZwLnBkZjB/BgNVHR8EeDB2MHSgcqBwhm5sZGFwOi8vbGRhcC5jaGluYXBvcnQuZ292LmNuOjM4OS9jbj1jcmwwMzAwMDAsb3U9Y3JsMDAsb3U9Y3JsLGM9Y24/Y2VydGlmaWNhdGVSZXZvY2F0aW9uTGlzdD9iYXNlP2NuPWNybDAzMDAwMDA+BggrBgEFBQcBAQQyMDAwLgYIKwYBBQUHMAGGImh0dHA6Ly9vY3NwLmNoaW5hcG9ydC5nb3YuY246ODgwMC8wOgYKKwYBBAGpQ2QFAQQsDCrmtbfljZfnnIHojaPoqonov5vlh7rlj6PotLjmmJPmnInpmZDlhazlj7gwEgYKKwYBBAGpQ2QFAwQEDAIwMTAiBgorBgEEAalDZAUIBBQMEjUxMjMyNDE5NjQxMDE3Mjk3WDAgBgorBgEEAalDZAUJBBIMEDAzLUpKMEc5MDAyMjA3NTIwGQYKKwYBBAGpQ2QFCwQLDAlNQTVUTkZHWTkwEgYKKwYBBAGpQ2QFDAQEDAIwMDASBgorBgEEAalDZAIBBAQMAjEyMBIGCisGAQQBqUNkAgQEBAwCMTQwDAYIKoEcz1UBg3UFAANIADBFAiBM4OVAc8aaCZU4XFfcVMkC7bWIIenRnPLxrnwVeYO3CQIhANQ767YIurkJCoLtwyqQPbUZe/+3BjGZcIWqB1mAl9T+"],"Error":[]}
        //验证签名: 成功!
    }

    @Test
    @DisplayName("取算法类型（取加密算法标识）")
    void t3() {
        HashMap<String, Object> _args = new HashMap<>();
        UkeyRequest ukeyRequest = new UkeyRequest("cus-sec_SpcGetCardID", _args);
        Args args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("获取卡号: " + JSONUtil.toJsonStr(args));

        _args = new HashMap<>();
        _args.put("lReader", args.getData().get(0));
        ukeyRequest = new UkeyRequest("ra_GetCryptAlgo", _args);
        args = signClient.getUkeyResponseArgs(ukeyRequest);
        System.err.println("取算法类型（取加密算法标识）" + args.getData().get(0));
    }

    @Test
    @DisplayName("证书切割")
    void t4() {
        String pom = "MIIEoDCCBESgAwIBAgIIAwAAAAAM3m8wDAYIKoEcz1UBg3UFADCBmDELMAkGA1UEBhMCQ04xDzANBgNVBAgMBuWMl+S6rDEPMA0GA1UEBwwG5YyX5LqsMRswGQYDVQQKDBLkuK3lm73nlLXlrZDlj6PlsrgxGzAZBgNVBAsMEuivgeS5pueuoeeQhuS4reW/gzEtMCsGA1UEAwwk5Lit5Zu955S15a2Q5Lia5Yqh6K+B5Lmm566h55CG5Lit5b+DMB4XDTIzMDMyOTAwMDAwMFoXDTMzMDMyOTAwMDAwMFowVjELMAkGA1UEBhMCQ04xMzAxBgNVBAsMKua1t+WNl+ecgeiNo+iqiei/m+WHuuWPo+i0uOaYk+aciemZkOWFrOWPuDESMBAGA1UEAwwJ5p2o5aaC6YeRMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE0vOQmplAr9igPZrA8F1msqnFd0U++6G6NhG5rNuIUWft0BwQn7eSJkt5/fvSSoe7pUg2/awHUWPnzkeeQc7oVqOCArUwggKxMBEGCWCGSAGG+EIBAQQEAwIFoDAOBgNVHQ8BAf8EBAMCBsAwCQYDVR0TBAIwADApBgNVHSUEIjAgBggrBgEFBQcDAgYIKwYBBQUHAwQGCisGAQQBgjcUAgIwHwYDVR0jBBgwFoAURCQxt0wEvoAVXmuo4N1bjKXTh0UwHQYDVR0OBBYEFAytGob5L0WqhOCZ5l6Lf2jUdNrAMGgGA1UdIARhMF8wXQYEVR0gADBVMFMGCCsGAQUFBwIBFkdodHRwczovL3d3dy5jaGluYXBvcnQuZ292LmNuL3RjbXNmaWxlL3UvY21zL3d3dy8yMDIyMDQvMTIxMzI5NDh4dDZwLnBkZjB/BgNVHR8EeDB2MHSgcqBwhm5sZGFwOi8vbGRhcC5jaGluYXBvcnQuZ292LmNuOjM4OS9jbj1jcmwwMzAwMDAsb3U9Y3JsMDAsb3U9Y3JsLGM9Y24/Y2VydGlmaWNhdGVSZXZvY2F0aW9uTGlzdD9iYXNlP2NuPWNybDAzMDAwMDA+BggrBgEFBQcBAQQyMDAwLgYIKwYBBQUHMAGGImh0dHA6Ly9vY3NwLmNoaW5hcG9ydC5nb3YuY246ODgwMC8wOgYKKwYBBAGpQ2QFAQQsDCrmtbfljZfnnIHojaPoqonov5vlh7rlj6PotLjmmJPmnInpmZDlhazlj7gwEgYKKwYBBAGpQ2QFAwQEDAIwMTAiBgorBgEEAalDZAUIBBQMEjUxMjMyNDE5NjQxMDE3Mjk3WDAgBgorBgEEAalDZAUJBBIMEDAzLUpKMEc5MDAyMjA3NTIwGQYKKwYBBAGpQ2QFCwQLDAlNQTVUTkZHWTkwEgYKKwYBBAGpQ2QFDAQEDAIwMDASBgorBgEEAalDZAIBBAQMAjEyMBIGCisGAQQBqUNkAgQEBAwCMTQwDAYIKoEcz1UBg3UFAANIADBFAiBM4OVAc8aaCZU4XFfcVMkC7bWIIenRnPLxrnwVeYO3CQIhANQ767YIurkJCoLtwyqQPbUZe/+3BjGZcIWqB1mAl9T+";
        CertificateHandler reportClient = SpringUtil.getBean(CertificateHandler.class);
        System.out.println(reportClient.buildX509Certificate(pom));
    }
}
