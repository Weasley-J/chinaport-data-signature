package cn.alphahub.eport.signature.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import static cn.alphahub.eport.signature.config.UkeyProperties.PREFIX;

/**
 * 电子口岸u-key的配置参数
 *
 * @author weasley
 * @version 1.0
 * @date 2022/2/13
 */
@Data
@Validated
@ConfigurationProperties(prefix = PREFIX)
public class UkeyProperties {
    public static final String PREFIX = "eport.signature.ukey";
    /**
     * socket链接url
     */
    private String wsUrl = "ws://127.0.0.1:61232";
    /**
     * u-key密码
     */
    private String password = "88888888";
}
