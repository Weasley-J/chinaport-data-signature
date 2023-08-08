package cn.alphahub.eport.signature.core.web;

import cn.alphahub.eport.signature.entity.Capture179DataResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Eport customs 179 http client
 *
 * @author weasley
 * @version 1.0.0
 */
@HttpExchange(accept = "application/json")
public interface EportCustoms179HttpClient {
    /**
     * 海关 179 数据抓取
     *
     * @param formData 表达数据
     */
    @PostExchange(value = "/ceb2grab/grab/realTimeDataUpload", contentType = "application/x-www-form-urlencoded")
    Mono<Capture179DataResponse> report179Data(@RequestParam Map<String, Object> formData);
}
