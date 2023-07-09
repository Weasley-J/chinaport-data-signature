package cn.alphahub.eport.signature.core;

import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

/**
 * 核心方法测试
 */
@SpringBootTest
class SignatureHandlerTest {

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

    @Resource
    private SignHandler signHandler;

    @Test
    void getSignatureValueBeforeSendToWebSocket() {
        SignRequest request = new SignRequest(CEB621Message);
        String payload = signHandler.getDynamicSignDataParameter(request);
        SignResult sign = signHandler.sign(request, payload);
        System.err.println(JSONUtil.toJsonPrettyStr(sign));
    }
}
