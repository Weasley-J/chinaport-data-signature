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
     * @response {"message":"操作成功","success":true,"timestamp":"2023-07-30 18:49:15","code":200,"data":{"success":true,"certNo":"03000000000cde6f","x509Certificate":"MIIEoDCCBESgAwIBAgIIAwAAAAAM3m8wDAYIKoEcz1UBg3UFADCBmDELMAkGA1UEBhMCQ04xDzANBgNVBAgMBuWMl+S6rDEPMA0GA1UEBwwG5YyX5LqsMRswGQYDVQQKDBLkuK3lm73nlLXlrZDlj6PlsrgxGzAZBgNVBAsMEuivgeS5pueuoeeQhuS4reW/gzEtMCsGA1UEAwwk5Lit5Zu955S15a2Q5Lia5Yqh6K+B5Lmm566h55CG5Lit5b+DMB4XDTIzMDMyOTAwMDAwMFoXDTMzMDMyOTAwMDAwMFowVjELMAkGA1UEBhMCQ04xMzAxBgNVBAsMKua1t+WNl+ecgeiNo+iqiei/m+WHuuWPo+i0uOaYk+aciemZkOWFrOWPuDESMBAGA1UEAwwJ5p2o5aaC6YeRMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE0vOQmplAr9igPZrA8F1msqnFd0U++6G6NhG5rNuIUWft0BwQn7eSJkt5/fvSSoe7pUg2/awHUWPnzkeeQc7oVqOCArUwggKxMBEGCWCGSAGG+EIBAQQEAwIFoDAOBgNVHQ8BAf8EBAMCBsAwCQYDVR0TBAIwADApBgNVHSUEIjAgBggrBgEFBQcDAgYIKwYBBQUHAwQGCisGAQQBgjcUAgIwHwYDVR0jBBgwFoAURCQxt0wEvoAVXmuo4N1bjKXTh0UwHQYDVR0OBBYEFAytGob5L0WqhOCZ5l6Lf2jUdNrAMGgGA1UdIARhMF8wXQYEVR0gADBVMFMGCCsGAQUFBwIBFkdodHRwczovL3d3dy5jaGluYXBvcnQuZ292LmNuL3RjbXNmaWxlL3UvY21zL3d3dy8yMDIyMDQvMTIxMzI5NDh4dDZwLnBkZjB/BgNVHR8EeDB2MHSgcqBwhm5sZGFwOi8vbGRhcC5jaGluYXBvcnQuZ292LmNuOjM4OS9jbj1jcmwwMzAwMDAsb3U9Y3JsMDAsb3U9Y3JsLGM9Y24/Y2VydGlmaWNhdGVSZXZvY2F0aW9uTGlzdD9iYXNlP2NuPWNybDAzMDAwMDA+BggrBgEFBQcBAQQyMDAwLgYIKwYBBQUHMAGGImh0dHA6Ly9vY3NwLmNoaW5hcG9ydC5nb3YuY246ODgwMC8wOgYKKwYBBAGpQ2QFAQQsDCrmtbfljZfnnIHojaPoqonov5vlh7rlj6PotLjmmJPmnInpmZDlhazlj7gwEgYKKwYBBAGpQ2QFAwQEDAIwMTAiBgorBgEEAalDZAUIBBQMEjUxMjMyNDE5NjQxMDE3Mjk3WDAgBgorBgEEAalDZAUJBBIMEDAzLUpKMEc5MDAyMjA3NTIwGQYKKwYBBAGpQ2QFCwQLDAlNQTVUTkZHWTkwEgYKKwYBBAGpQ2QFDAQEDAIwMDASBgorBgEEAalDZAIBBAQMAjEyMBIGCisGAQQBqUNkAgQEBAwCMTQwDAYIKoEcz1UBg3UFAANIADBFAiBM4OVAc8aaCZU4XFfcVMkC7bWIIenRnPLxrnwVeYO3CQIhANQ767YIurkJCoLtwyqQPbUZe/+3BjGZcIWqB1mAl9T+","digestValue":"/uy5whbEnIhnrSkF7hSAJNm8ISI=","signatureValue":"uEG8qUT4NMS7g+QVJK6WdVQXlSK5n94PQJ+iGWec2cCyXfKh6Il2U+jmEq6nxWpoxCn3M0zNH7bICaoKrMpc6w==","signatureNode":"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ds:SignedInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">\n    <ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>\n    <ds:SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sm2-sm3\"/>\n    <ds:Reference URI=\"\">\n        <ds:Transforms>\n            <ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>\n        </ds:Transforms>\n        <ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>\n        <ds:DigestValue>/uy5whbEnIhnrSkF7hSAJNm8ISI=</ds:DigestValue>\n    </ds:Reference>\n</ds:SignedInfo>"}}
     */
    @GetMapping("/cebmessage/signature")
    public Result<SignResult> signCebMessage() {
        @SuppressWarnings({"all"}) String sourceXml = "<ceb:CEB621Message xmlns:ceb=\"http://www.chinaport.gov.cn/ceb\" guid=\"CEB621_HNZB_FXJK_20220208175054_0035\" version=\"v1.0\">\n" +
                "    <ceb:Inventory>\n" +
                "        <ceb:InventoryHead>\n" +
                "            <ceb:guid>CEB621_HNZB_FXJK_20220208175055_0035</ceb:guid>\n" +
                "            <ceb:appType>1</ceb:appType>\n" +
                "            <ceb:appTime>20220208175055</ceb:appTime>\n" +
                "            <ceb:appStatus>2</ceb:appStatus>\n" +
                "            <ceb:orderNo>48037605991776256</ceb:orderNo>\n" +
                "            <ceb:ebpCode>46121601BC</ceb:ebpCode>\n" +
                "            <ceb:ebpName>海南星创互联网医药有限公司</ceb:ebpName>\n" +
                "            <ceb:ebcCode>46121601BC</ceb:ebcCode>\n" +
                "            <ceb:ebcName>海南星创互联网医药有限公司</ceb:ebcName>\n" +
                "            <ceb:logisticsNo>2222222</ceb:logisticsNo>\n" +
                "            <ceb:logisticsCode>4101986180</ceb:logisticsCode>\n" +
                "            <ceb:logisticsName>圆通</ceb:logisticsName>\n" +
                "            <ceb:copNo>4101W68006</ceb:copNo>\n" +
                "            <ceb:assureCode>4101960AGP</ceb:assureCode>\n" +
                "            <ceb:ieFlag>I</ceb:ieFlag>\n" +
                "            <ceb:declTime>20220208</ceb:declTime>\n" +
                "            <ceb:customsCode>4601</ceb:customsCode>\n" +
                "            <ceb:portCode>4601</ceb:portCode>\n" +
                "            <ceb:ieDate>20220208</ceb:ieDate>\n" +
                "            <ceb:buyerIdType>1</ceb:buyerIdType>\n" +
                "            <ceb:buyerIdNumber>612326199005306719</ceb:buyerIdNumber>\n" +
                "            <ceb:buyerName>用户_15150368</ceb:buyerName>\n" +
                "            <ceb:buyerTelephone>18209182500</ceb:buyerTelephone>\n" +
                "            <ceb:consigneeAddress>哈布斯堡</ceb:consigneeAddress>\n" +
                "            <ceb:agentCode>46121601BC</ceb:agentCode>\n" +
                "            <ceb:agentName>海南星创互联网医药有限公司</ceb:agentName>\n" +
                "            <ceb:tradeMode>9610</ceb:tradeMode>\n" +
                "            <ceb:trafMode>W</ceb:trafMode>\n" +
                "            <ceb:country>142</ceb:country>\n" +
                "            <ceb:freight>0.00</ceb:freight>\n" +
                "            <ceb:insuredFee>0</ceb:insuredFee>\n" +
                "            <ceb:currency>142</ceb:currency>\n" +
                "            <ceb:packNo>1</ceb:packNo>\n" +
                "            <ceb:grossWeight>0.07</ceb:grossWeight>\n" +
                "            <ceb:netWeight>0.06</ceb:netWeight>\n" +
                "        </ceb:InventoryHead>\n" +
                "        <ceb:InventoryList>\n" +
                "            <ceb:gnum>1</ceb:gnum>\n" +
                "            <ceb:itemRecordNo>0797776159482</ceb:itemRecordNo>\n" +
                "            <ceb:itemNo>HNGGKJ00000057</ceb:itemNo>\n" +
                "            <ceb:itemName>通用铁剂胶囊</ceb:itemName>\n" +
                "            <ceb:gcode>2106909090</ceb:gcode>\n" +
                "            <ceb:gname>ACTIVE IRON</ceb:gname>\n" +
                "            <ceb:gmodel>4|3|乳清蛋白(牛奶)65%,胶囊壳(羟苯基甲基纤维素)21%,硫酸亚铁1.2%,酸度调节剂2%,填充剂 10.8%|30粒/盒|无中文名称/ACTIVE IRON</ceb:gmodel>\n" +
                "            <ceb:country>306</ceb:country>\n" +
                "            <ceb:currency>142</ceb:currency>\n" +
                "            <ceb:qty>1</ceb:qty>\n" +
                "            <ceb:unit>140</ceb:unit>\n" +
                "            <ceb:qty1>0.06</ceb:qty1>\n" +
                "            <ceb:unit1>035</ceb:unit1>\n" +
                "            <ceb:price>0.01</ceb:price>\n" +
                "            <ceb:totalPrice>0.01</ceb:totalPrice>\n" +
                "        </ceb:InventoryList>\n" +
                "    </ceb:Inventory>\n" +
                "    <ceb:BaseTransfer>\n" +
                "        <ceb:copCode>46121601BC</ceb:copCode>\n" +
                "        <ceb:copName>海南星创互联网医药有限公司</ceb:copName>\n" +
                "        <ceb:dxpMode>DXP</ceb:dxpMode>\n" +
                "        <ceb:dxpId>DXPENT0000458763</ceb:dxpId>\n" +
                "    </ceb:BaseTransfer>\n" +
                "</ceb:CEB621Message>";
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
     * @response {
     * "message": "操作成功",
     * "success": true,
     * "timestamp": "2023-07-28 21:38:43",
     * "code": 200,
     * "data": {
     * "success": true,
     * "certNo": "03000000000cde6f",
     * "signatureValue": "05mobHSrNqcy0ZNVH484S80dnusjP9fD2esnYTiTQKy8/O6LGSYAYRMRzI+p1vEAVSFt+F0s3jYI6c5W7dZMzA=="
     * }
     * }
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
