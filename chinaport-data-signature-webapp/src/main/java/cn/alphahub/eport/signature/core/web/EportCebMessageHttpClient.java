package cn.alphahub.eport.signature.core.web;

import io.github.weasleyj.china.eport.sign.model.request.MessageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

/**
 * Eport ceb message http client
 *
 * @author weasley
 * @version 1.0.0
 */
@HttpExchange(accept = "application/json", contentType = "application/json")
public interface EportCebMessageHttpClient {
    /**
     * 上报海关总署 CEBMessage XML 数据
     */
    @PostExchange(value = "/cebcmsg")
    Mono<String> reportCebMessage(@RequestBody MessageRequest messageRequest);
}
