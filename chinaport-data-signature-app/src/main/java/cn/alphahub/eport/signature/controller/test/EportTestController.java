package cn.alphahub.eport.signature.controller.test;

import cn.alphahub.dtt.plus.util.SpringUtil;
import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.base.exception.SignException;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.support.CommandClient;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * 电子口岸报文加签测试
 *
 * @author lwj
 * @version 1.1.0
 */
@Slf4j
@RestController
@RequestMapping("/rpc/eport/test")
public class EportTestController {

    @Autowired
    private SignHandler signHandler;

    /**
     * 海关总署XML数据加签
     *
     * @return 数据加签结果
     * @apiNote 非正式调用API，只为了让你看到海关总署XML加密的数据返回格式
     */
    @GetMapping("/cebmessage/signature")
    public Result<SignResult> signCebMessage() {
        @SuppressWarnings("all") String sourceXml = "<ceb:CEB621Message xmlns:ceb=\"http://www.chinaport.gov.cn/ceb\" guid=\"CEB621_HNZB_FXJK_20220208175054_0034\" version=\"v1.0\">\n" +
                "                    <ceb:Inventory>\n" +
                "                        <ceb:InventoryHead>\n" +
                "                            <ceb:guid>CEB621_HNZB_FXJK_20220208175055_0035</ceb:guid>\n" +
                "                            <ceb:appType>1</ceb:appType>\n" +
                "                            <ceb:appTime>20220208175055</ceb:appTime>\n" +
                "                            <ceb:appStatus>2</ceb:appStatus>\n" +
                "                            <ceb:orderNo>48037605991776256</ceb:orderNo>\n" +
                "                            <ceb:ebpCode>46121601BC</ceb:ebpCode>\n" +
                "                            <ceb:ebpName>海南星创互联网医药有限公司</ceb:ebpName>\n" +
                "                            <ceb:ebcCode>46121601BC</ceb:ebcCode>\n" +
                "                            <ceb:ebcName>海南星创互联网医药有限公司</ceb:ebcName>\n" +
                "                            <ceb:logisticsNo>2222222</ceb:logisticsNo>\n" +
                "                            <ceb:logisticsCode>4101986180</ceb:logisticsCode>\n" +
                "                            <ceb:logisticsName>圆通</ceb:logisticsName>\n" +
                "                            <ceb:copNo>4101W68006</ceb:copNo>\n" +
                "                            <ceb:assureCode>4101960AGP</ceb:assureCode>\n" +
                "                            <ceb:ieFlag>I</ceb:ieFlag>\n" +
                "                            <ceb:declTime>20220208</ceb:declTime>\n" +
                "                            <ceb:customsCode>4601</ceb:customsCode>\n" +
                "                            <ceb:portCode>4601</ceb:portCode>\n" +
                "                            <ceb:ieDate>20220208</ceb:ieDate>\n" +
                "                            <ceb:buyerIdType>1</ceb:buyerIdType>\n" +
                "                            <ceb:buyerIdNumber>612326199005306719</ceb:buyerIdNumber>\n" +
                "                            <ceb:buyerName>用户_15150368</ceb:buyerName>\n" +
                "                            <ceb:buyerTelephone>18209182500</ceb:buyerTelephone>\n" +
                "                            <ceb:consigneeAddress>哈布斯堡</ceb:consigneeAddress>\n" +
                "                            <ceb:agentCode>46121601BC</ceb:agentCode>\n" +
                "                            <ceb:agentName>海南星创互联网医药有限公司</ceb:agentName>\n" +
                "                            <ceb:tradeMode>9610</ceb:tradeMode>\n" +
                "                            <ceb:trafMode>W</ceb:trafMode>\n" +
                "                            <ceb:country>142</ceb:country>\n" +
                "                            <ceb:freight>0.00</ceb:freight>\n" +
                "                            <ceb:insuredFee>0</ceb:insuredFee>\n" +
                "                            <ceb:currency>142</ceb:currency>\n" +
                "                            <ceb:packNo>1</ceb:packNo>\n" +
                "                            <ceb:grossWeight>0.07</ceb:grossWeight>\n" +
                "                            <ceb:netWeight>0.06</ceb:netWeight>\n" +
                "                        </ceb:InventoryHead>\n" +
                "                        <ceb:InventoryList>\n" +
                "                            <ceb:gnum>1</ceb:gnum>\n" +
                "                            <ceb:itemRecordNo>0797776159482</ceb:itemRecordNo>\n" +
                "                            <ceb:itemNo>HNGGKJ00000057</ceb:itemNo>\n" +
                "                            <ceb:itemName>通用铁剂胶囊</ceb:itemName>\n" +
                "                            <ceb:gcode>2106909090</ceb:gcode>\n" +
                "                            <ceb:gname>ACTIVE IRON</ceb:gname>\n" +
                "                            <ceb:gmodel>4|3|乳清蛋白(牛奶)65%,胶囊壳(羟苯基甲基纤维素)21%,硫酸亚铁1.2%,酸度调节剂2%,填充剂 10.8%|30粒/盒|无中文名称/ACTIVE IRON</ceb:gmodel>\n" +
                "                            <ceb:country>306</ceb:country>\n" +
                "                            <ceb:currency>142</ceb:currency>\n" +
                "                            <ceb:qty>1</ceb:qty>\n" +
                "                            <ceb:unit>140</ceb:unit>\n" +
                "                            <ceb:qty1>0.06</ceb:qty1>\n" +
                "                            <ceb:unit1>035</ceb:unit1>\n" +
                "                            <ceb:price>0.01</ceb:price>\n" +
                "                            <ceb:totalPrice>0.01</ceb:totalPrice>\n" +
                "                        </ceb:InventoryList>\n" +
                "                    </ceb:Inventory>\n" +
                "                    <ceb:BaseTransfer>\n" +
                "                        <ceb:copCode>46121601BC</ceb:copCode>\n" +
                "                        <ceb:copName>海南星创互联网医药有限公司</ceb:copName>\n" +
                "                        <ceb:dxpMode>DXP</ceb:dxpMode>\n" +
                "                        <ceb:dxpId>DXPENT0000458763</ceb:dxpId>\n" +
                "                    </ceb:BaseTransfer>\n" +
                "                </ceb:CEB621Message>";
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
     * @return 数据加签结果
     * @apiNote 非正式调用API，只为了让你看到179加密的数据返回格式
     */
    @GetMapping("/179/signature")
    public Result<SignResult> sign179Report() {
        String sign179String = "\"sessionID\":\"ad2254-8hewyf32-55616249\"||\"payExchangeInfoHead\":\"{\"guid\":\"9D55BA71-22DE-41F4-8B50-C36C83B3B530\",\"initalRequest\":\"原始请求\",\"initalResponse\":\"ok\",\"ebpCode\":\"4404840022\",\"payCode\":\"312226T001\",\"payTransactionId\":\"2018121222001354081010726129\",\"totalAmount\":100,\"currency\":\"142\",\"verDept\":\"3\",\"payType\":\"1\",\"tradingTime\":\"20181212041803\",\"note\":\"批量订单，测试订单优化,生成多个so订单\"}\"||\"payExchangeInfoLists\":\"[{\"orderNo\":\"SO1710301150602574003\",\"goodsInfo\":[{\"gname\":\"lhy-gnsku3\",\"itemLink\":\"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999761&shopId=453\"},{\"gname\":\"lhy-gnsku2\",\"itemLink\":\"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999760&shopId=453\"}],\"recpAccount\":\"OSA571908863132601\",\"recpCode\":\"\",\"recpName\":\"YUNJIHONGKONGLIMITED\"}]\"||\"serviceTime\":\"1544519952469\"";
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
     * @apiNote 此接口测试环境、生产环境不对外开放，会在将终端输出同步写给浏览器
     */
    @GetMapping("/endpoint/exec")
    public void execute(@RequestParam("cmd") String cmd, HttpServletResponse response) throws IOException {
        if (ObjectUtils.isNotEmpty(SpringUtil.getActiveProfiles()) && Set.of(SpringUtil.getActiveProfiles()).contains("prod")) {
            throw new SignException(NOT_FOUND.getReasonPhrase(), NOT_FOUND.value());
        }
        log.info("执行脚本: {}", cmd);
        response.addHeader("Content-Type", "text/event-stream; charset=utf-8");
        CommandClient commandClient = CommandClient.getSharedInstance();
        commandClient.execute(cmd, response.getOutputStream());
    }

}
