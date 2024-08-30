package cn.alphahub.eport.signature.config;

import cn.alphahub.eport.signature.base.exception.EportWebClientException;
import cn.alphahub.eport.signature.core.web.EportCebMessageHttpClient;
import cn.alphahub.eport.signature.core.web.EportCustoms179HttpClient;
import cn.alphahub.eport.signature.core.web.EportReportResultHttpClient;
import cn.alphahub.eport.signature.core.web.WebfluxDemoHttpClient;
import cn.alphahub.eport.signature.support.OriginalPropertyNamingStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

import static cn.alphahub.eport.signature.core.ChinaEportReportClient.EPORT_CEBMESSAGE_SERVER_ENCODE;
import static cn.alphahub.eport.signature.core.ChinaEportReportClient.REPORT_PROD_ENV_179_URL_ENCODE;

/**
 * The WebClient Configuration
 *
 * @author weasley
 * @version 1.0.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({Customs179Properties.class, ChinaEportProperties.class})
public class WebClientConfiguration {
    public static final ThreadLocal<Consumer<HttpHeaders>> HTTP_HEADERS = new ThreadLocal<>();

    /**
     * @return A proxy bean of EportCebMessageHttpClient
     */
    @Bean
    public EportCebMessageHttpClient eportCebMessageHttpClient(ObjectMapper objectMapper, ChinaEportProperties chinaEportProperties) {
        WebClient webClient = WebClient.builder()
                .exchangeStrategies(customExchangeStrategies(objectMapper))
                .filter(((request, next) -> next.exchange(Objects.requireNonNull(injectHeader(request).block()))))
                .filters(exchangeFilters -> {
                    exchangeFilters.add(logRequest());
                    exchangeFilters.add(logResponse());
                })
                .defaultStatusHandler(HttpStatusCode::isError, resp -> Mono.just(new EportWebClientException("Web Client 调用发生异常!")))
                .baseUrl(StringUtils.defaultIfBlank(chinaEportProperties.getServer(), new String(Base64.decodeBase64(EPORT_CEBMESSAGE_SERVER_ENCODE))))
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient))
                .build();
        return httpServiceProxyFactory.createClient(EportCebMessageHttpClient.class);
    }


    /**
     * @return A proxy bean of EportCustoms179HttpClient
     */
    @Bean
    public EportCustoms179HttpClient eportCustoms179HttpClient(ObjectMapper objectMapper, Customs179Properties customs179Properties) {
        WebClient webClient = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder().codecs(configurer -> {
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(enhanceSourceMapper(objectMapper)));
                }).build())
                .filters(exchangeFilters -> {
                    exchangeFilters.add(((request, next) -> next.exchange(Objects.requireNonNull(injectHeader(request).block()))));
                    exchangeFilters.add(logRequest());
                    exchangeFilters.add(logResponse());
                })
                .defaultStatusHandler(HttpStatusCode::isError, resp -> Mono.just(new EportWebClientException("Web Client 调用发生异常!")))
                .baseUrl(StringUtils.defaultIfBlank(customs179Properties.getServer(), new String(Base64.decodeBase64(REPORT_PROD_ENV_179_URL_ENCODE))))
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build();
        return httpServiceProxyFactory.createClient(EportCustoms179HttpClient.class);
    }

    /**
     * @return A proxy bean of EportReportResultHttpClient
     */
    @Bean
    public EportReportResultHttpClient eportReportResultHttpClient(ChinaEportProperties chinaEportProperties) {
        String baseUrl = StringUtils.defaultIfBlank(chinaEportProperties.getServer(), new String(Base64.decodeBase64(EPORT_CEBMESSAGE_SERVER_ENCODE)));
        WebClient webClient = WebClient.builder()
                .filter(logRequest())
                .filter(logResponse())
                .defaultStatusHandler(HttpStatusCode::isError, resp -> Mono.just(new EportWebClientException("Web Client 调用发生异常!")))
                .baseUrl(baseUrl)
                .build();
        return HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build()
                .createClient(EportReportResultHttpClient.class);
    }

    /**
     * @return A proxy bean of WebfluxDemoHttpClient
     */
    @Bean
    public WebfluxDemoHttpClient webfluxDemoHttpClient(ObjectMapper objectMapper) {
        WebClient webClient = WebClient.builder()
                .exchangeStrategies(customExchangeStrategies(objectMapper))
                .filters(exchangeFilters -> {
                    exchangeFilters.add(((request, next) -> next.exchange(Objects.requireNonNull(injectHeader(request).block()))));
                    exchangeFilters.add(logRequest());
                    exchangeFilters.add(logResponse());
                })
                .defaultStatusHandler(HttpStatusCode::isError, resp -> Mono.just(new EportWebClientException("Web Client 调用发生异常!")))
                .baseUrl("http://127.0.0.1:8080")
                .build();
        WebClientAdapter clientAdapter = WebClientAdapter.forClient(webClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder(clientAdapter).build();
        return httpServiceProxyFactory.createClient(WebfluxDemoHttpClient.class);
    }

    /**
     * To inject headers
     */
    public Mono<ClientRequest> injectHeader(final ClientRequest clientRequest) {
        if (null != HTTP_HEADERS.get()) {
            Mono<ClientRequest> clientRequestMono = Mono.just(ClientRequest.from(clientRequest)
                    .headers(HTTP_HEADERS.get())
                    .build());
            clearHttpHeaders();
            return clientRequestMono;
        }
        return Mono.just(ClientRequest.from(clientRequest)
                .build());
    }

    /**
     * Clear Http Headers
     */
    public void clearHttpHeaders() {
        if (null != HTTP_HEADERS.get()) {
            HTTP_HEADERS.remove();
        }
    }

    /**
     * Custom exchange strategies
     */
    public ExchangeStrategies customExchangeStrategies(ObjectMapper objectMapper) {
        return ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(enhanceSourceMapper(objectMapper)));
                }).build();
    }

    /**
     * To enhance mapper
     */
    private ObjectMapper enhanceSourceMapper(ObjectMapper objectMapper) {
        ObjectMapper enhanceMapper = objectMapper.copy();
        enhanceMapper.setPropertyNamingStrategy(new OriginalPropertyNamingStrategy());
        return enhanceMapper;
    }

    /**
     * log web request
     */
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (log.isInfoEnabled()) {
                log.info("Request URL: {}", clientRequest.url());
                log.info("Request Method: {}", clientRequest.method());
                log.info("Request Headers: {}", clientRequest.headers());
                if (clientRequest.method() == HttpMethod.POST || clientRequest.method() == HttpMethod.PUT) {
                    log.info("Request Body: {}", clientRequest.body());
                }
            }
            return Mono.just(clientRequest);
        });
    }

    /**
     * log web response
     */
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (log.isInfoEnabled()) {
                log.info("Response status: {}", clientResponse.statusCode());
                log.info("Response headers: {}", clientResponse.headers().asHttpHeaders());
            }
            return Mono.just(clientResponse);
        });
    }

}
