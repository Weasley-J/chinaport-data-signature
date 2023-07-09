package cn.alphahub.eport.signature.client;

import cn.alphahub.eport.signature.config.UkeyInitialConfig;
import cn.alphahub.eport.signature.config.UkeyInitialConfigTest;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


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

    @Test
    void sendInitDataAsPEMWithHashPTest() {
        SignRequest request = new SignRequest(UkeyInitialConfigTest.CEB621Message);
        String parameter = UkeyInitialConfig.getSignDataAsPEMParameter(request);
        SignHandler client = new SignHandler();
        SignResult result = client.sign(request, parameter);
        System.out.println(JSONUtil.toJsonPrettyStr(result));
    }

    @Test
    void sendInitDataNoHashAsPEMParameterTest() {
        SignRequest request = new SignRequest(UkeyInitialConfigTest.CEB621Message);
        String parameter = UkeyInitialConfig.getSignDataNoHashAsPEMParameter(request);
        SignHandler client = new SignHandler();
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
        SignHandler client = new SignHandler();
        SignResult result = client.sign(request, "666");
        System.out.println(JSONUtil.toJsonPrettyStr(result));
    }
}
