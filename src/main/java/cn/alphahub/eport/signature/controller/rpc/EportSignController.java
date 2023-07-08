package cn.alphahub.eport.signature.controller.rpc;

import cn.alphahub.eport.signature.basic.domain.Result;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 电子口岸报文加签Controller
 *
 * @author lwj
 * @version 1.0.0
 * @date 2022-01-11 15:29
 */
@Slf4j
@RestController
@RequestMapping("/rpc/eport")
public class EportSignController {

    @Resource
    private SignHandler signHandler;

    /**
     * 海关数据加签
     *
     * @param request 加签数据请求入参
     * @return 签名结果
     * @apiNote 此接口已经整合"海关总署XML"和"海关179数据抓取"的加签<br/>
     * <ul><b>支持的加签类型</b><li>1. 海关CEBXxxMessage XML数据加签</li><li>2. 海关179数据加签</li></ul>
     */
    @PostMapping("/signature")
    public Result<SignResult> signature(@RequestBody @Validated SignRequest request) {
        String payload = signHandler.getDynamicSignDataParameter(request);
        SignResult signed = signHandler.sign(request, payload);
        log.info("加签响应 {}", JSONUtil.toJsonStr(signed));
        if (signed.getSuccess().equals(false)) {
            return Result.error("加签失败");
        }
        return Result.success(signed);
    }

}
