package cn.alphahub.eport.signature.core.web;

import cn.alphahub.dtt.plus.util.JacksonUtil;
import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.config.WebClientConfiguration;
import cn.alphahub.eport.signature.entity.SignResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static cn.alphahub.eport.signature.config.AuthenticationProperties.AUTHENTICATION_HEADER;

@Slf4j
@SpringBootTest
class WebfluxDemoHttpClientTest {

    @Autowired
    private WebfluxDemoHttpClient webfluxDemoHttpClient;

    @Test
    @DisplayName("验证WebClient调用")
    void signCebMessage() {
        String headerValue = DigestUtils.md5DigestAsHex("chinaport-data-signature".getBytes(StandardCharsets.UTF_8));
        WebClientConfiguration.HTTP_HEADERS.set(headers -> {
            headers.add(AUTHENTICATION_HEADER, headerValue);
        });
        Result<SignResult> result = webfluxDemoHttpClient.signCebMessage();
        log.info("Sign ceb message: {}", JacksonUtil.toPrettyJson(result));
        Assertions.assertNotNull(webfluxDemoHttpClient);
    }

}
