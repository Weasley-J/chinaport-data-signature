package cn.alphahub.eport.signature.core;

import cn.alphahub.eport.signature.entity.UkeyResponse;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebSocketClientHandlerTest {

    @Autowired
    WebSocketClientHandler handler;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void handleTextMessage() throws Exception {
        UkeyResponse response = JSONUtil.toBean("{\"_id\":1,\"_method\":\"cus-sec_SpcSignDataAsPEM\",\"_status\":\"00\",\"_args\":{\"Result\":false,\"Data\":[],\"Error\":[\"[读卡器底层库]复位读卡器失败:错误码=50070\",\"Err:Custom50070\"]}}",
                new TypeReference<>() {
                }, true);
        handler.handleFailedToProcessSign(JSONUtil.toJsonPrettyStr(response));
    }
}
