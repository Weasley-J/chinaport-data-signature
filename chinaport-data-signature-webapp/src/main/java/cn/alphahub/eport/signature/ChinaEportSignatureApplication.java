package cn.alphahub.eport.signature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * 中国电子口岸（海口海关）报文加签推送应用
 *
 * @author Weasley J
 * @date 2022-02-13
 * @since 1.0.0
 */
@EnableWebSocket
@SpringBootApplication
public class ChinaEportSignatureApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChinaEportSignatureApplication.class, args);
    }
}
