package cn.alphahub.eport.signature.after202207;

import cn.alphahub.eport.signature.entity.UkeyRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 测试获取证书与对应的签名方法
 *
 * @author weasley
 * @version 1.0.0
 */
@SpringBootTest
public class TestSignCertAndSignDataMethod {
    public static final String pwd = "88888888";

    @Autowired
    private SignClient signClient;

    public static void main(String[] args) {
        /* 1 签名方法  **/
        // 签名,返回PEM格式
        UkeyRequest u1 = new UkeyRequest("cus-sec_SpcSignDataAsPEM", new LinkedHashMap<>() {{
            put("inData", SignConstant.sign179);
            put("passwd", pwd);
        }});

        /*
         * 签名,不对原文计算摘要,请您自行计算好摘要传入
         * @param inData 原文的SHA1摘要
         */
        UkeyRequest u2 = new UkeyRequest("cus-sec_SpcSignDataNoHash", new LinkedHashMap<>() {{
            put("inData", SignConstant.sign179);
            put("passwd", pwd);
        }});

        /*
         * 签名,不对原文计算摘要,请您自行计算好摘要传入
         * 返回PEM格式数据
         * @param inData 原文的SHA1摘要
         */
        UkeyRequest u3 = new UkeyRequest("cus-sec_SpcSignDataNoHashAsPEM", new LinkedHashMap<>() {{
            put("inData", SignConstant.sign179);
            put("passwd", pwd);
        }});

        /* 2 获取证书  **/
        //取海关签名证书
        UkeyRequest c1 = new UkeyRequest("cus-sec_SpcGetSignCert", new HashMap<>());
        //取海关签名证书PEM
        UkeyRequest c2 = new UkeyRequest("cus-sec_SpcGetSignCertAsPEM", new HashMap<>());

        //todo 验证
        /*
         验证：
         u1 -> c1,c2
         u2 -> c1,c2
         u3 -> c1,c2
         */
    }

    @Test
    void u1c1() {
    }

    @Test
    void u1c2() {
    }

    @Test
    void u2c1() {
    }

    @Test
    void u2c2() {
    }

    @Test
    void u3c1() {
    }

    @Test
    void u3c2() {
    }

}
