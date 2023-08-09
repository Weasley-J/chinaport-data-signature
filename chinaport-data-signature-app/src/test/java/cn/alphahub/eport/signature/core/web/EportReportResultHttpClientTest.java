package cn.alphahub.eport.signature.core.web;

import cn.alphahub.eport.signature.config.ChinaEportProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.format.DateTimeFormatter;

@Slf4j
@SpringBootTest
class EportReportResultHttpClientTest {

    @Autowired
    ChinaEportProperties chinaEportProperties;
    @Autowired
    EportReportResultHttpClient eportReportResultHttpClient;
    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Test
    @DisplayName("查询 311 申报回执")
    void getCeb312msg() {
        String result = eportReportResultHttpClient.getCeb312msgResult(chinaEportProperties.getDxpId(), "20230806121200");
        log.info("311回执结果：{}", result);
        Assertions.assertNotNull(result, "311回执结果不为空");
    }

    @Test
    @DisplayName("查询 621 申报回执")
    void getCe622msg() {
        String result = eportReportResultHttpClient.getCe622msgResult(chinaEportProperties.getDxpId(), "20230806121200");
        log.info("621回执结果：{}", result);
        Assertions.assertNotNull(result, "621回执结果不为空");
    }
}
