package cn.alphahub.eport.signature.client;

import cn.alphahub.eport.signature.config.InitialConfig;
import cn.alphahub.eport.signature.config.InitialConfigTest;
import cn.alphahub.eport.signature.core.EportSignHandler;
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
class StandardEportSignHandlerTest {

    @Test
    void sendInitDataAsPEMWithHashPTest() {
        SignRequest request = new SignRequest(null, InitialConfigTest.CEB621Message);
        String parameter = InitialConfig.getSignDataAsPEMParameter(request);
        EportSignHandler client = new EportSignHandler();
        SignResult result = client.sign(request, parameter);
        System.out.println(JSONUtil.toJsonPrettyStr(result));
    }

    @Test
    void sendInitDataNoHashAsPEMParameterTest() {
        SignRequest request = new SignRequest(null, InitialConfigTest.CEB621Message);
        String parameter = InitialConfig.getSignDataNoHashAsPEMParameter(request);
        EportSignHandler client = new EportSignHandler();
        SignResult result = client.sign(request, parameter);
        System.out.println(JSONUtil.toJsonPrettyStr(result));
    }

}
