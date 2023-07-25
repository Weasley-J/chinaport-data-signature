package cn.alphahub.eport.signature.config;

import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.util.ClientIPUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import static cn.alphahub.dtt.plus.util.JacksonUtil.toJson;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

/**
 * Rate Limiter Web Mvc Configuration
 *
 * @author weasley
 * @version 1.1.0
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class RateLimiterWebMvcConfiguration implements WebMvcConfigurer {
    private final IpRateLimiterManager ipRateLimiterManager;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimiterInterceptor(ipRateLimiterManager)).addPathPatterns("/**").excludePathPatterns("/rpc/**");
    }

    /**
     * 限流拦截器
     *
     * @author weasley
     * @version 1.1.0
     */
    @Slf4j
    public static class RateLimiterInterceptor implements HandlerInterceptor {
        private final IpRateLimiterManager ipRateLimiterManager;

        public RateLimiterInterceptor(IpRateLimiterManager ipRateLimiterManager) {
            this.ipRateLimiterManager = ipRateLimiterManager;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            RateLimiter limiter = ipRateLimiterManager.getRateLimiterForIp(request);
            if (limiter.tryAcquire()) {
                return true;
            } else {
                log.warn("触发限流，客户端IP: {}", ClientIPUtils.getClientIP(request));
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.println(toJson(Result.error(TOO_MANY_REQUESTS.value(), TOO_MANY_REQUESTS.getReasonPhrase())));
                writer.flush();
                writer.close();
                return false;
            }
        }
    }

    /**
     * Ip Rate Limiter Manager
     *
     * @author weasley
     * @version 1.1.0
     */
    @Component
    public static class IpRateLimiterManager {
        /**
         * 每个IP地址限流: permitsPerSecond 个请求/秒
         */
        private static double permitsPerSecond = 10.0;
        private final Cache<String, RateLimiter> ipRateLimiters;

        /**
         * 设置过期时间，1分钟内没有活跃请求的IP会自动过期
         */
        public IpRateLimiterManager() {
            ipRateLimiters = Caffeine.newBuilder()
                    .expireAfterWrite(1, TimeUnit.MINUTES)
                    .build();
        }

        public RateLimiter getRateLimiterForIp(HttpServletRequest request) {
            String clientIP = ClientIPUtils.getClientIP(request);
            return ipRateLimiters.get(clientIP, this::createRateLimiter);
        }

        private RateLimiter createRateLimiter(String ip) {
            return RateLimiter.create(permitsPerSecond);
        }
    }
}
