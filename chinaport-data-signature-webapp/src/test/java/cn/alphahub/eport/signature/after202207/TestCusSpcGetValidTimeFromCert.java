package cn.alphahub.eport.signature.after202207;

import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查看海关证书有效期
 *
 * @author weasley
 * @version 1.0.7
 */
@SpringBootTest
public class TestCusSpcGetValidTimeFromCert {
    @Autowired
    private SignClient signClient;

    @Test
    @DisplayName("查看海关证书有效期")
    void testCusSpcGetValidTimeFromCert() {
        Map<String, Object> args = new LinkedHashMap<>();
        //args.put("cert", null);//证书内容,可为空
        UkeyRequest ukeyRequest = new UkeyRequest("cus-sec_SpcGetValidTimeFromCert", 1, args);
        JSONConfig jsonConfig = new JSONConfig();
        String jsonStr = JSONUtil.toJsonStr(ukeyRequest, jsonConfig);
        System.err.println(jsonStr);
        signClient.sign(ukeyRequest);
    }
}
