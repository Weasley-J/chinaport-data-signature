package cn.alphahub.eport.signature.controller.rpc;

import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.core.ChinaEportReportClient;
import cn.alphahub.eport.signature.entity.Capture179DataRequest;
import cn.alphahub.eport.signature.entity.Capture179DataResponse;
import cn.alphahub.eport.signature.entity.ThirdAbstractResponse;
import cn.alphahub.eport.signature.entity.UploadCEBMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 电子口岸报文推送
 *
 * @author weasley
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/rpc/eport/upload")
public class EportUploadController {

    @Autowired
    private ChinaEportReportClient chinaEportReportClient;

    /**
     * 推送报文CEBMessage报文
     *
     * @param request CEBMessage报文
     * @return 结果，OK表示已推送
     */
    @PostMapping("/CEBMessage")
    public Result<ThirdAbstractResponse<String, String>> uploadCEBMessage(@RequestBody @Validated UploadCEBMessageRequest request) {
        ThirdAbstractResponse<String, String> report = chinaEportReportClient.report(chinaEportReportClient.getCebMessageByMessageType(request), request.getMessageType());
        return Result.ok(report);
    }

    /**
     * 海关179数据抓取
     */
    @PostMapping("/179/data")
    public Result<ThirdAbstractResponse<String, Capture179DataResponse>> capture179Data(@RequestBody @Validated Capture179DataRequest request) {
        ThirdAbstractResponse<String, Capture179DataResponse> report = chinaEportReportClient.capture179Data(request);
        return Result.ok(report);
    }

}
