package cn.alphahub.eport.signature.after202207;

import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SignClient
 *
 * @author weasley
 * @version 1.0.0
 */
@Service
public class SignClient {
    @Autowired
    private SignHandler signHandler;

    /**
     * 和U-key交互, 用179数据测试
     */
    public SignResult sign(UkeyRequest ukeyRequest) {
        JSONConfig jsonConfig = new JSONConfig();
        String params = JSONUtil.toJsonStr(ukeyRequest, jsonConfig);
        SignRequest request = new SignRequest(SignConstant.sign179);
        SignResult result = signHandler.sign(request, params);
        System.err.println(result);
        return result;
    }
}
