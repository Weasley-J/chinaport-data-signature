package cn.alphahub.eport.signature.config;

import cn.alphahub.eport.signature.basic.domain.Result;
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

import static cn.alphahub.dtt.plus.util.JacksonUtil.toPrettyJson;
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
        registry.addInterceptor(new AuthenticationInterceptor(authenticationProperties)).addPathPatterns("/**");
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
            boolean unauthorized = StringUtils.isBlank(request.getHeader(AUTHENTICATION_HEADER))
                    || !StringUtils.equals(request.getHeader(AUTHENTICATION_HEADER), authenticationProperties.getToken());
            if (unauthorized) {
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                Result<Object> error = Result.error(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase(), "Please add '" + AUTHENTICATION_HEADER + "' to your HTTP request header");
                writer.println(toPrettyJson(error));
                writer.flush();
                writer.close();
                return false;
            }
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
    }
}
