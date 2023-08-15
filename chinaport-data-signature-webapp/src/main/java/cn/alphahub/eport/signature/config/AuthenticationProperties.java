package cn.alphahub.eport.signature.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


/**
 * 加签程序Token鉴权配置元数据
 *
 * @author weasley
 * @version 1.0.0
 */
@Data
@Validated
@ConfigurationProperties(prefix = AuthenticationProperties.PREFIX)
public final class AuthenticationProperties {
    public static final String PREFIX = "eport.signature.auth";
    /**
     * 客户端加签时携带的请求头，值必须和配置文件里面配置鉴权token相等
     *
     * @see AuthenticationProperties#token
     */
    public static final String AUTHENTICATION_HEADER = "x-auth-token-eport-sign";
    /**
     * 默认鉴权token
     *
     * @apiNote 生产环境不要配这个，强加密的字符串
     */
    public static final String DEFAULT_AUTH_TOKEN = "DefaultAuthToken";
    /**
     * 是否启用token鉴权
     */
    private Boolean enable = true;
    /**
     * 客户端请求鉴权token
     *
     * @apiNote 自行生成token字符串，如：md5，sha1Hex，...
     */
    private String token = DEFAULT_AUTH_TOKEN;
}
