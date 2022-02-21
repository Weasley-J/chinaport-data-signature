package cn.alphahub.eport.signature.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * 电子口岸u-key的配置参数
 *
 * @author weasley
 * @version 1.0
 * @date 2022/2/13
 */
@Data
@Validated
@ConfigurationProperties(prefix = "eport.signature.ukey")
public class UkeyProperties {
    /**
     * u-key的.cer证书在classpath的相对路径
     */
    private String certPath = "";
    /**
     * socket链接url
     */
    private String wsUrl = "ws://127.0.0.1:61232";
    /**
     * u-key密码
     */
    private String password = "88888888";
}
