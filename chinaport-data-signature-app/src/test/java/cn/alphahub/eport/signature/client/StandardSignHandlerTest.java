package cn.alphahub.eport.signature.client;

import cn.alphahub.eport.signature.config.UkeyInitialConfig;
import cn.alphahub.eport.signature.config.UkeyInitialConfigTest;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;

import static cn.alphahub.dtt.plus.util.JacksonUtil.toJson;


/**
 * WebSocketClient测试类
 *
 * @author weasley
 * @version 1.0
 * @date 2022/2/12
 */
@Slf4j
@SpringBootTest
class StandardSignHandlerTest {
    @Autowired
    private SignHandler client;

    @Test
    void sendInitDataAsPEMWithHashPTest() {
        SignRequest request = new SignRequest(UkeyInitialConfigTest.CEB621Message);
        String parameter = UkeyInitialConfig.getSignDataAsPEMParameter(request);
        SignResult result = client.sign(request, parameter);
        System.out.println(JSONUtil.toJsonPrettyStr(result));
    }

    @Test
    void sendInitDataNoHashAsPEMParameterTest() {
        SignRequest request = new SignRequest(UkeyInitialConfigTest.CEB621Message);
        String parameter = UkeyInitialConfig.getSignDataNoHashAsPEMParameter(request);
        SignResult result = client.sign(request, parameter);
        System.out.println(JSONUtil.toJsonPrettyStr(result));
    }

    /**
     * 获取版本号
     */
    @Test
    void ukeyVersion() {
        String method = "cus-sec_SpcGetCardAttachInfo";
        SignRequest request = new SignRequest(UkeyInitialConfigTest.CEB621Message);
        SignResult result = client.sign(request, "666");
        System.out.println(JSONUtil.toJsonPrettyStr(result));
    }

    /**
     * 取算法类型（取加密算法标识）
     *
     * @implNote lReader 取指定卡的信息，1取RA卡，其它值则取被制卡或KEY
     */
    @Test
    @DisplayName("取算法类型（取加密算法标识）")
    void ukeyEncryptionAlgorithmIdentification() {
        String method = "ra_GetCryptAlgo";
        UkeyRequest ukeyRequest = new UkeyRequest(method, new LinkedHashMap<>() {{
            put("inData", "");
            put("lReader", 1);
        }});
        String signParams = toJson(ukeyRequest);
        SignRequest request = new SignRequest("");
        SignResult result = client.sign(request, signParams);
        System.out.println(JSONUtil.toJsonPrettyStr(result));
    }

}
