package cn.alphahub.eport.signature.config;

import cn.alphahub.dtt.plus.util.JacksonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * email config 测试类
 */
@SpringBootTest
class EmailPropertiesTest {

    @Autowired
    EmailProperties emailProperties;

    @Autowired
    MailProperties mailProperties;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void contextLoads() {
        Boolean enable = emailProperties.getEnable();
        System.out.println("enable = " + enable);
    }

    @Test
    void getCcEmails() {
        System.out.println(emailProperties.getCc());
        System.out.println(JacksonUtil.toPrettyJson(emailProperties.getCcEmails()));
    }
}
