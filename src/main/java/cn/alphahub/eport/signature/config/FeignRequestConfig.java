package cn.alphahub.eport.signature.config;

import com.google.common.net.HttpHeaders;
import feign.Logger;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * <b>Feign请求模板增强配置类</b>
 * <ul>
 *    <li>2. 同步客户端携带过来的所有请求头信息</li>
 * </ul>
 *
 * @author liuwenjing
 * @version 1.0
 */
@Slf4j
@Configuration
public class FeignRequestConfig {

    /**
     * <b>Feign请求拦截器</b>
     * <ul>
     *     <li>Feign远程调用前先进入RequestInterceptor.apply()方法</li>
     *     <li>RequestContextHolder获取当前请求的上下文, 基于ThreadLocal存储RequestContext</li>
     * </ul>
     *
     * @return 同步客户端请求头后的Feign请求拦截器
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (Objects.nonNull(requestAttributes)) {
                HttpServletRequest request = requestAttributes.getRequest();
                String originalHeader = request.getHeader(HttpHeaders.COOKIE);
                requestTemplate.header(HttpHeaders.COOKIE, originalHeader);
            }
        };
    }

    /**
     * @return Log the headers, body, and metadata for both requests and responses.
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}