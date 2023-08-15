package cn.alphahub.eport.signature.core.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

/**
 * 查询CEB Message申报回执Http客户端
 *
 * @author weasley
 * @version 1.0.0
 */
@HttpExchange
public interface EportReportResultHttpClient {
    /**
     * 查询 311 申报回执
     * <p>
     * 返回示例:
     * <pre>
     * [
     *   {
     *     org_id=1,
     *     instm=1644919105000,
     *     re_order_guid=CEB311_HNZB_FXJK_20220215175237_0013,
     *     returnstatus=2,
     *     ebpcode=46121601BC,
     *     returntime2=20220215175825,
     *     ebccode=46121601BC,
     *     trans_dxpid=DXPENT0000458763,
     *     returninfo=新增申报成功
     *   [
     *     CEB311_HNZB_FXJK_20220215175237_0014
     *   ],
     *   returntime=1644919100000,
     *   orderno=20220215148803180891992064
     *   }
     * ]
     * </pre>
     *
     * @param dxpid 向中国电子口岸数据中心申请数据交换平台的用户编号
     * @param qryid 查询时间，日期时间格式：yyyyMMddHHmmss
     * @apiNote 查询此时间点之后的数据，必须在当前时间点之前
     */
    @GetExchange(value = "/ceb312msg", accept = {MediaType.TEXT_PLAIN_VALUE})
    Mono<String> getCeb312msgResult(@RequestParam String dxpid, @RequestParam String qryid);

    /**
     * 查询 621 申报回执
     *
     * @param dxpid 向中国电子口岸数据中心申请数据交换平台的用户编号
     * @param qryid 查询时间，日期时间格式：yyyyMMddHHmmss
     * @apiNote 查询此时间点之后的数据，必须在当前时间点之前
     */
    @GetExchange(value = "/ceb622msg", accept = {MediaType.TEXT_PLAIN_VALUE})
    Mono<String> getCe622msgResult(@RequestParam String dxpid, @RequestParam String qryid);
}
