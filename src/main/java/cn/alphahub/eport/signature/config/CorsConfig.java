package cn.alphahub.eport.signature.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

/**
 * 跨域配置类，解决ajax请求跨域问题
 * <p>Spring WebFlux Java config CORS 配置的替代方案
 *
 * @author liuwenjing
 * @version 1.1.3
 * @date 2022年2月13日
 */
@Configuration
public class CorsConfig {

    /**
     * @return Spring WebFlux Java config CORS配置的{@code CorsWebFilter}
     */
    @Bean
    public CorsWebFilter corsWebFilter() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
        config.setAllowedMethods(Collections.singletonList(CorsConfiguration.ALL));
        config.setAllowedOriginPatterns(Collections.singletonList(CorsConfiguration.ALL));
        config.setExposedHeaders(List.of(HttpHeaders.SET_COOKIE));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}