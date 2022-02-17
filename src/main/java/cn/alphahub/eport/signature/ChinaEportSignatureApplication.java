package cn.alphahub.eport.signature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 中国电子口岸（海口海关CEBXxxMessage）加签服务
 *
 * @author Weasley J
 * @date 2022-02-13
 */
@EnableScheduling
@SpringBootApplication
public class ChinaEportSignatureApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChinaEportSignatureApplication.class, args);
    }
}
