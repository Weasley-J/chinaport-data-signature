package cn.alphahub.eport.signature.controller.rpc;

import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.config.ChinaEportProperties;
import cn.alphahub.eport.signature.core.web.EportReportResultHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 查询申报回执结果
 *
 * @author weasley
 * @version 1.1.0
 * @apiNote 目前仅实现：CEB311Message、CEB621Message 申报结果查询
 */
@Slf4j
@RestController
@RequestMapping("/rpc/eport/result")
public class EportReportResultController {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    @Autowired
    private ChinaEportProperties chinaEportProperties;
    @Autowired
    private EportReportResultHttpClient eportReportResultHttpClient;

    /**
     * 查询311进口单申报结果
     *
     * @param qryId 查询时间，日期时间格式：yyyyMMddHHmmss
     * @response {
     * "message": "操作成功",
     * "success": true,
     * "timestamp": "2023-08-09 15:18:25",
     * "code": 200,
     * "data": "[{org_id=1, instm=1691397836000, re_order_guid=NWIVQA-WEASLEY-20230807164347-GCVZ4S, returnstatus=2, ebpcode=46016602EV, returntime2=20230807164356, ebccode=46016602EV, trans_dxpid=DXPENT0000530815, returninfo=新增申报成功[NWIVQA-WEASLEY-20230807164347-GCVZ4S], returntime=1691397830000, orderno=JO_D8079556739228400022}]"
     * }
     * @apiNote 查询此时间点之后的数据，必须在当前时间点之前，默认查询当前时间前15秒的申报数据，当前时间：20230806144417，则 qryId=20230806144401，不传的情况适用于刚刚申报成功查询回执结果，海关的处理时间间隔约为15秒
     * @since 1.1.1
     */
    @GetMapping("/ceb312msg")
    public Result<String> getCeb312msgResult(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMddHHmmss") LocalDateTime qryId) {
        qryId = ObjectUtils.defaultIfNull(qryId, LocalDateTime.now().minusSeconds(15));
        log.info("开始查询311申报回执结果, qryId {}", FORMATTER.format(qryId));
        Mono<String> msgResult = eportReportResultHttpClient.getCeb312msgResult(chinaEportProperties.getDxpId(), FORMATTER.format(qryId));
        return Result.ok(msgResult.block());
    }

    /**
     * 查询621进口单申报结果
     *
     * @param qryId 查询时间，日期时间格式：yyyyMMddHHmmss
     * @response {
     * "message": "操作成功",
     * "success": true,
     * "timestamp": "2023-08-09 15:26:53",
     * "code": 200,
     * "data": "[{org_id=1, instm=1691397837000, agentcode=46016602EV, re_elist_guid_cus=9422f96b-ccbc-4a69-a899-6d9dd583d501, returnstatus=2, ebpcode=46016602EV, preno=B20230807570793612, returntime2=20230807164357, ebccode=46016602EV, createtime=20230807164351, trans_dxpid=DXPENT0000530815, returninfo=清单新增申报成功[3CQHDG-WEASLEY-20230807164348-7KYRQN][电商企业编码：46016602EV订单编号：JO_D8079556739228400022], returntime=1691397830000, re_elist_guid=3CQHDG-WEASLEY-20230807164348-7KYRQN, customscode=6409}, {org_id=1, instm=1691397847000, agentcode=46016602EV, re_elist_guid_cus=9422f96b-ccbc-4a69-a899-6d9dd583d501, returnstatus=120, ebpcode=46016602EV, preno=B20230807570793612, returntime2=20230807164407, ebccode=46016602EV, createtime=20230807164355, trans_dxpid=DXPENT0000530815, returninfo=[Code:1800;Desc:逻辑校验通过], returntime=1691397831000, re_elist_guid=4e7a2dab-064b-410b-9ce6-e322c590ae3d, customscode=6409, invtno=64092023I000722460}, {org_id=1, instm=1691397847000, agentcode=46016602EV, re_elist_guid_cus=9422f96b-ccbc-4a69-a899-6d9dd583d501, returnstatus=800, ebpcode=46016602EV, preno=B20230807570793612, returntime2=20230807164407, ebccode=46016602EV, createtime=20230807164355, trans_dxpid=DXPENT0000530815, returninfo=[Code:2600;Desc:放行], returntime=1691397832000, re_elist_guid=4e7a2dab-064b-410b-9ce6-e322c590ae3d, customscode=6409, invtno=64092023I000722460}]"
     * }
     * @apiNote 查询此时间点之后的数据，必须在当前时间点之前，默认查询当前时间前15秒的申报数据，当前时间：20230806144417，则 qryId=20230806144401，不传的情况适用于刚刚申报成功查询回执结果，海关的处理时间间隔约为15秒
     * @since 1.1.1
     */
    @GetMapping("/ceb622msg")
    public Result<String> getCe622msgResult(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMddHHmmss") LocalDateTime qryId) {
        qryId = ObjectUtils.defaultIfNull(qryId, LocalDateTime.now().minusSeconds(15));
        log.info("开始查询621进口单申报回执结果, qryId {}", FORMATTER.format(qryId));
        Mono<String> msgResult = eportReportResultHttpClient.getCe622msgResult(chinaEportProperties.getDxpId(), FORMATTER.format(qryId));
        return Result.ok(msgResult.block());
    }

}
