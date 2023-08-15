package cn.alphahub.eport.signature.config;

import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.util.ClientIPUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.PrintWriter;

import static cn.alphahub.dtt.plus.util.JacksonUtil.toJson;
import static cn.alphahub.eport.signature.config.AuthenticationProperties.AUTHENTICATION_HEADER;
import static cn.alphahub.eport.signature.config.AuthenticationProperties.PREFIX;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Authentication Web Mvc Configuration
 *
 * @author weasley
 * @version 1.1.0
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({AuthenticationProperties.class})
@ConditionalOnProperty(prefix = PREFIX, name = {"enable"}, havingValue = "true")
public class AuthWebMvcConfiguration implements WebMvcConfigurer {

    private final AuthenticationProperties authenticationProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(authenticationProperties)).addPathPatterns("/rpc/**");
    }

    /**
     * 客户端Token鉴权拦截器
     *
     * @author weasley
     * @version 1.1.0
     */
    @Slf4j
    @AllArgsConstructor
    public static class AuthenticationInterceptor implements HandlerInterceptor {
        private final AuthenticationProperties authenticationProperties;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            boolean unauthorized = false;
            String message = null;
            if (StringUtils.isBlank(request.getHeader(AUTHENTICATION_HEADER))) {
                unauthorized = true;
                message = "Missing request header: " + AUTHENTICATION_HEADER;
            } else if (!StringUtils.equals(request.getHeader(AUTHENTICATION_HEADER), authenticationProperties.getToken())) {
                unauthorized = true;
                message = "Invalid token: " + request.getHeader(AUTHENTICATION_HEADER);
            }
            if (unauthorized) {
                log.warn("{}, 客户端IP: {}", message, ClientIPUtils.getClientIP(request));
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.println(toJson(Result.error(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase())));
                writer.flush();
                writer.close();
                return false;
            }
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
    }
}
