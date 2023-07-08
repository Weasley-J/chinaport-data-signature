package cn.alphahub.eport.signature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 中国电子口岸加签用用
 *
 * @author Weasley J
 * @date 2022-02-13
 * @since 1.0.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ChinaEportSignatureApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChinaEportSignatureApplication.class, args);
    }
}
