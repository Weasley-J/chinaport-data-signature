package cn.alphahub.eport.signature.controller.test;

import cn.alphahub.dtt.plus.util.SpringUtil;
import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.base.exception.SignException;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.support.CommandClient;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * 电子口岸报文加签测试接口
 *
 * @author lwj
 * @version 1.1.0
 */
@Slf4j
@RestController
@RequestMapping("/rpc/eport/test")
public class EportTestController {

    @Resource
    private SignHandler signHandler;

    /**
     * 海关总署XML数据加签
     *
     * @return 签名结果
     * @apiNote 非正式调用API，只为了让你看到海关总署XML加密的数据返回格式
     */
    @GetMapping("/cebmessage/signature")
    public Result<SignResult> signCebMessage() {
        String sourceXml = """
                <ceb:CEB621Message xmlns:ceb="http://www.chinaport.gov.cn/ceb" guid="CEB621_HNZB_FXJK_20220208175054_0034" version="v1.0">
                    <ceb:Inventory>
                        <ceb:InventoryHead>
                            <ceb:guid>CEB621_HNZB_FXJK_20220208175055_0035</ceb:guid>
                            <ceb:appType>1</ceb:appType>
                            <ceb:appTime>20220208175055</ceb:appTime>
                            <ceb:appStatus>2</ceb:appStatus>
                            <ceb:orderNo>48037605991776256</ceb:orderNo>
                            <ceb:ebpCode>46121601BC</ceb:ebpCode>
                            <ceb:ebpName>海南星创互联网医药有限公司</ceb:ebpName>
                            <ceb:ebcCode>46121601BC</ceb:ebcCode>
                            <ceb:ebcName>海南星创互联网医药有限公司</ceb:ebcName>
                            <ceb:logisticsNo>2222222</ceb:logisticsNo>
                            <ceb:logisticsCode>4101986180</ceb:logisticsCode>
                            <ceb:logisticsName>圆通</ceb:logisticsName>
                            <ceb:copNo>4101W68006</ceb:copNo>
                            <ceb:assureCode>4101960AGP</ceb:assureCode>
                            <ceb:ieFlag>I</ceb:ieFlag>
                            <ceb:declTime>20220208</ceb:declTime>
                            <ceb:customsCode>4601</ceb:customsCode>
                            <ceb:portCode>4601</ceb:portCode>
                            <ceb:ieDate>20220208</ceb:ieDate>
                            <ceb:buyerIdType>1</ceb:buyerIdType>
                            <ceb:buyerIdNumber>612326199005306719</ceb:buyerIdNumber>
                            <ceb:buyerName>用户_15150368</ceb:buyerName>
                            <ceb:buyerTelephone>18209182500</ceb:buyerTelephone>
                            <ceb:consigneeAddress>哈布斯堡</ceb:consigneeAddress>
                            <ceb:agentCode>46121601BC</ceb:agentCode>
                            <ceb:agentName>海南星创互联网医药有限公司</ceb:agentName>
                            <ceb:tradeMode>9610</ceb:tradeMode>
                            <ceb:trafMode>W</ceb:trafMode>
                            <ceb:country>142</ceb:country>
                            <ceb:freight>0.00</ceb:freight>
                            <ceb:insuredFee>0</ceb:insuredFee>
                            <ceb:currency>142</ceb:currency>
                            <ceb:packNo>1</ceb:packNo>
                            <ceb:grossWeight>0.07</ceb:grossWeight>
                            <ceb:netWeight>0.06</ceb:netWeight>
                        </ceb:InventoryHead>
                        <ceb:InventoryList>
                            <ceb:gnum>1</ceb:gnum>
                            <ceb:itemRecordNo>0797776159482</ceb:itemRecordNo>
                            <ceb:itemNo>HNGGKJ00000057</ceb:itemNo>
                            <ceb:itemName>通用铁剂胶囊</ceb:itemName>
                            <ceb:gcode>2106909090</ceb:gcode>
                            <ceb:gname>ACTIVE IRON</ceb:gname>
                            <ceb:gmodel>4|3|乳清蛋白(牛奶)65%,胶囊壳(羟苯基甲基纤维素)21%,硫酸亚铁1.2%,酸度调节剂2%,填充剂 10.8%|30粒/盒|无中文名称/ACTIVE IRON</ceb:gmodel>
                            <ceb:country>306</ceb:country>
                            <ceb:currency>142</ceb:currency>
                            <ceb:qty>1</ceb:qty>
                            <ceb:unit>140</ceb:unit>
                            <ceb:qty1>0.06</ceb:qty1>
                            <ceb:unit1>035</ceb:unit1>
                            <ceb:price>0.01</ceb:price>
                            <ceb:totalPrice>0.01</ceb:totalPrice>
                        </ceb:InventoryList>
                    </ceb:Inventory>
                    <ceb:BaseTransfer>
                        <ceb:copCode>46121601BC</ceb:copCode>
                        <ceb:copName>海南星创互联网医药有限公司</ceb:copName>
                        <ceb:dxpMode>DXP</ceb:dxpMode>
                        <ceb:dxpId>DXPENT0000458763</ceb:dxpId>
                    </ceb:BaseTransfer>
                </ceb:CEB621Message>
                """;
        SignRequest request = new SignRequest(sourceXml);
        String payload = signHandler.getSignDataParameter(request);
        SignResult signed = signHandler.sign(request, payload);
        log.info("加签结果响应体 {}", JSONUtil.toJsonStr(signed));
        if (signed.getSuccess().equals(false)) {
            return Result.error("加签失败");
        }
        return Result.success(signed);
    }

    /**
     * 海关179数据抓取加签
     *
     * @return 签名结果
     * @apiNote 非正式调用API，只为了让你看到179加密的数据返回格式
     */
    @GetMapping("/179/signature")
    public Result<SignResult> sign179Report() {
        String sign179String = """
                "sessionID":"ad2254-8hewyf32-55616249"||"payExchangeInfoHead":"{"guid":"9D55BA71-22DE-41F4-8B50-C36C83B3B530","initalRequest":"原始请求","initalResponse":"ok","ebpCode":"4404840022","payCode":"312226T001","payTransactionId":"2018121222001354081010726129","totalAmount":100,"currency":"142","verDept":"3","payType":"1","tradingTime":"20181212041803","note":"批量订单，测试订单优化,生成多个so订单"}"||"payExchangeInfoLists":"[{"orderNo":"SO1710301150602574003","goodsInfo":[{"gname":"lhy-gnsku3","itemLink":"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999761&shopId=453"},{"gname":"lhy-gnsku2","itemLink":"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999760&shopId=453"}],"recpAccount":"OSA571908863132601","recpCode":"","recpName":"YUNJIHONGKONGLIMITED"}]"||"serviceTime":"1544519952469"
                """;
        SignRequest request = new SignRequest(sign179String);
        String payload = signHandler.getSignDataParameter(request);
        SignResult signed = signHandler.sign(request, payload);
        log.info("加签响应 {}", JSONUtil.toJsonStr(signed));
        if (signed.getSuccess().equals(false)) {
            return Result.error("加签失败");
        }
        return Result.ok(signed);
    }

    /**
     * 执行脚本命令
     *
     * @apiNote 此接口测试环境、生产环境不对外开放（危险），会在将终端输出同步写给浏览器
     */
    @GetMapping("/endpoint/exec")
    public void execute(@RequestParam("cmd") String cmd, HttpServletResponse response) throws IOException {
        if (ObjectUtils.isNotEmpty(SpringUtil.getActiveProfiles()) &&
                Set.of(SpringUtil.getActiveProfiles()).contains("prod")) {
            throw new SignException(NOT_FOUND.getReasonPhrase(), NOT_FOUND.value());
        }
        log.info("执行脚本: {}", cmd);
        response.addHeader("Content-Type", "text/event-stream; charset=utf-8");
        CommandClient commandClient = CommandClient.getSharedInstance();
        commandClient.execute(cmd, response.getOutputStream());
    }

}
