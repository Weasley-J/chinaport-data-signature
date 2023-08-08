package cn.alphahub.eport.signature.core.web;

import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.entity.SignResult;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * Local Demo Http Client
 *
 * @author weasley
 * @version 1.0.0
 */
@HttpExchange(accept = "application/json", contentType = "application/json")
public interface WebfluxDemoHttpClient {
    /**
     * 海关总署XML数据加签
     */
    @GetExchange("/rpc/eport/test/cebmessage/signature")
    Result<SignResult> signCebMessage();
}
