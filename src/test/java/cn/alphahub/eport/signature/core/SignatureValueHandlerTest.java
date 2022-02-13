package cn.alphahub.eport.signature.core;

import cn.hutool.core.io.IoUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

/**
 * 核心方法测试
 */
@SpringBootTest
class SignatureValueHandlerTest {

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

    /*@Resource
    WebsocketSignatureService service;

    @Test
    void getSignatureValueBeforeSendToWebSocket() {
        SignResult sign = service.sign(new SignRequest(CEB621Message));
        System.err.println(JSONUtil.toJsonPrettyStr(sign));
    }*/
}
