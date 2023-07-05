package cn.alphahub.eport.signature.report.dock179;


import cn.alphahub.eport.signature.after202207.SignConstant;
import cn.alphahub.eport.signature.controller.rpc.EportSignController;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.alphahub.eport.signature.report.dock179.entity.Report179Request;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static cn.alphahub.dtt.plus.util.JacksonUtil.toJson;
import static cn.alphahub.eport.signature.core.CertificateHandler.METHOD_OF_X509_WITH_HASH;

@Slf4j
@SpringBootTest
class Upload179Tests {

    @Autowired
    private SignHandler signHandler;

    @Autowired
    private EportSignController signController;

    @Test
    void report() {
        Report179Request report179Request = JSONUtil.toBean("""
                        {
                          "sessionID": "032C3F56-0EE6-4558-B548-6C7A3451F07D",
                          "payExchangeInfoHead": {
                            "guid": "E4766021-21AC-1AA2-21DD-E974DF93C11D",
                            "initalRequest": "<xml><appid><![CDATA[xwxwxwxwx112121]]></appid><mch_id>11111111</mch_id><body><![CDATA[测试测试]]></body><nonce_str><![CDATA[ashdgahgdhaghgliqwueyqu1]]></nonce_str><out_trade_no>202009231454212140210352</out_trade_no><total_fee>500</total_fee><spbill_create_ip><![CDATA[192.168.0.1]]></spbill_create_ip><notify_url><![CDATA[https://www.baidu.com]]></notify_url><openid><![CDATA[adasyduiyasdhjxzycua-Q]]></openid><trade_type><![CDATA[JSAPI]]></trade_type><sign><![CDATA[asd1sa56d4545wqe44wq5]]></sign></xml>",
                            "initalResponse": "<xml><appid><![CDATA[xwxwxwxwx112121]]></appid><bank_type><![CDATA[OTHERS]]></bank_type><cash_fee>500</cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id>11111111</mch_id><nonce_str><![CDATA[ashdgahgdhaghgliqwueyqu1]]></nonce_str><openid><![CDATA[adasyduiyasdhjxzycua-Q]]></openid><out_trade_no>202009231454212140210352</out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[asgdhasgdhasgdhgasgdhasgdh]]></sign><time_end>20200923145426</time_end><total_fee>500</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id>4200000681202009235085032319</transaction_id></xml>",
                            "ebpCode": "请替换",
                            "payCode": "4403169D3W",
                            "payTransactionId": "4200000681202009235085032319",
                            "totalAmount": 5,
                            "currency": "142",
                            "verDept": "3",
                            "payType": "4",
                            "tradingTime": "20200923145426",
                            "note": ""
                          },
                          "payExchangeInfoLists": [
                            {
                              "orderNo": "202009231454218421271832",
                              "goodsInfo": [
                                {
                                  "gname": "济州花梨精华面膜",
                                  "itemLink": "http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999761&shopId=453"
                                }
                              ],
                              "recpAccount": "请替换",
                              "recpCode": "请替换",
                              "recpName": "请替换"
                            }
                          ],
                          "serviceTime": "1601282210417"
                        }
                        """
                , Report179Request.class);

        report179Request.setSessionID(IdUtil.fastSimpleUUID());

        String sign179String = """
                "sessionID":"ad2254-8hewyf32-55616249"||"payExchangeInfoHead":"{"guid":"9D55BA71-22DE-41F4-8B50-C36C83B3B530","initalRequest":"原始请求","initalResponse":"ok","ebpCode":"4404840022","payCode":"312226T001","payTransactionId":"2018121222001354081010726129","totalAmount":100,"currency":"142","verDept":"3","payType":"1","tradingTime":"20181212041803","note":"批量订单，测试订单优化,生成多个so订单"}"||"payExchangeInfoLists":"[{"orderNo":"SO1710301150602574003","goodsInfo":[{"gname":"lhy-gnsku3","itemLink":"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999761&shopId=453"},{"gname":"lhy-gnsku2","itemLink":"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999760&shopId=453"}],"recpAccount":"OSA571908863132601","recpCode":"","recpName":"YUNJIHONGKONGLIMITED"}]"||"serviceTime":"1544519952469"
                """;

        // 将 server-url base64加密下，不适合直接公布到外网
        String REPORT_TEST_SERVER_URL_encode = "aHR0cHM6Ly9zd2FwcHRlc3Quc2luZ2xld2luZG93LmNuL2NlYjJncmFiL2dyYWIvcmVhbFRpbWVEYXRhVXBsb2Fk";
        String REPORT_PROD_SERVER_URL_encode = "aHR0cHM6Ly9jdXN0b21zLmNoaW5hcG9ydC5nb3YuY24vY2ViMmdyYWIvZ3JhYi9yZWFsVGltZURhdGFVcGxvYWQ=";

        String REPORT_TEST_SERVER_URL = Base64.decodeStr(REPORT_TEST_SERVER_URL_encode);
        String REPORT_PROD_SERVER_URL = Base64.decodeStr(REPORT_PROD_SERVER_URL_encode);

        // 加签
        String dataOf179 = "\"sessionID\":\"" + report179Request.getSessionID() + "\"||" +
                "\"payExchangeInfoHead\":\"" + toJson(report179Request.getPayExchangeInfoHead()) + "\"||" +
                "\"payExchangeInfoLists\":\"" + toJson(report179Request.getPayExchangeInfoLists()) + "\"||" +
                "\"serviceTime\":" + "\"" + System.currentTimeMillis() + "\"";

        UkeyRequest ukeyRequest = new UkeyRequest(METHOD_OF_X509_WITH_HASH, new LinkedHashMap<>() {{
            //海关179数据抓取上报报文原始数据,数据格式: {"_method":"cus-sec_SpcSignDataAsPEM","_id":1,"args":{"inData":"{\n  \\\"sessionID\\\": \\\"fe2374-8fnejf97-32839218\\\",\n  \\\"payExchangeInfoHead\\\": {\n    \\\"guid\\\": \\\"fe2374-8fnejf97-32839218\\\",\n    \\\"initalRequest\\\": \\\"<xml><appid>wxa3412c979c7c6fb7</appid><mch_id>1537923071</mch_id><nonce_str>20220214148382001542606848</nonce_str><sign>38198AD6D22E59B3CCC683D67D8BB402</sign><sign_type>MD5</sign_type><body>测试订单编号411312450000027161</body><attach><![CDATA[16dbeaa5-92d8-47f5-a0a8-813a4d45a5f9]]></attach><out_trade_no>20220214148382001542606848</out_trade_no><total_fee>1</total_fee><spbill_create_ip>10.0.12.208</spbill_create_ip><notify_url>https://boss-dev.fengyouhui.net/be/api/mall-payment-c/api/public/gateway/payment/notice/wx/pay/20220214148382001542606848</notify_url><trade_type>MWEB</trade_type><openid></openid></xml>\\\",\n    \\\"initalResponse\\\": \\\"<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><result_code><![CDATA[SUCCESS]]></result_code><mch_id><![CDATA[1537923071]]></mch_id><appid><![CDATA[wxa3412c979c7c6fb7]]></appid><nonce_str><![CDATA[6SgxJ98xsr1nUnRG]]></nonce_str><sign><![CDATA[EB632B9C9F8BCB8027ACE9195878EE4F]]></sign><prepay_id><![CDATA[wx14105706563206ab24a00689936c800000]]></prepay_id><trade_type><![CDATA[MWEB]]></trade_type><mweb_url><![CDATA[https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx14105706563206ab24a00689936c800000&package=2518465012]]></mweb_url></xml>\\\",\n    \\\"ebpCode\\\": \\\"46121601BC\\\",\n    \\\"payCode\\\": \\\"1537923071\\\",\n    \\\"payTransactionId\\\": \\\"20220214148382001542606848\\\",\n    \\\"totalAmount\\\": 0.01,\n    \\\"currency\\\": \\\"502\\\",\n    \\\"verDept\\\": \\\"3\\\",\n    \\\"payType\\\": \\\"1\\\",\n    \\\"tradingTime\\\": \\\"20220214105714\\\",\n    \\\"note\\\": \\\"备注\\\"\n  },\n  \\\"payExchangeInfoLists\\\": [\n    {\n      \\\"orderNo\\\": \\\"\\\",\n      \\\"goodsInfo\\\": [\n        {\n          \\\"gname\\\": \\\"小米盒子\\\",\n          \\\"itemLink\\\": \\\"\\\"\n        }\n      ],\n      \\\"recpAccount\\\": \\\"1537923071\\\",\n      \\\"recpCode\\\": \\\"1537923071\\\",\n      \\\"recpName\\\": \\\"河南风友汇实业有限公司商户\\\"\n    }\n  ],\n  \\\"serviceTime\\\": 1533282603450\n}","passwd":"88888888"}}
            put("inData", dataOf179);
        }});
        String signParams = toJson(ukeyRequest);
        SignRequest request = new SignRequest(SignConstant.sign179);
        //SignResult result = signController.signature(request).getData();
        SignResult result = signHandler.sign(request, signParams);

        report179Request.setCertNo(result.getCertNo());
        report179Request.setSignValue(result.getSignatureValue());

        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("payExInfoStr", toJson(report179Request));
        log.info("server-url: {}\nbegin report，request data is ：{}", REPORT_PROD_SERVER_URL, toJson(paramMap));
        try {
            HttpResponse response = HttpUtil.createPost(REPORT_PROD_SERVER_URL)
                    .form(paramMap)
                    .header("Content-Type", "application/x-www-form-urlencoded", Boolean.FALSE)
                    .timeout(5 * 1000)
                    .execute();
            System.out.println(JSONUtil.toJsonPrettyStr(response.body()));
        } catch (Exception e) {
            log.error("report failure: {}", e.getLocalizedMessage(), e);
        }
    }

    public String concatOriginSignVale(String sessionId, Map<Object, Object> payExchangeInfoHead, List<Map<Object, Object>> payExchangeInfoLists, long serviceTime) {
        // 加签
        String info = "\"sessionID\":\"" + sessionId + "\"||" +
                "\"payExchangeInfoHead\":\"" + toJson(payExchangeInfoHead) + "\"||" +
                "\"payExchangeInfoLists\":\"" + toJson(payExchangeInfoLists) + "\"||" +
                "\"serviceTime\":" + "\"" + serviceTime + "\"";
        // optimize 密码
        return "{\"_method\":\"cus-sec_SpcSignDataAsPEM\",\"_id\":1,\"args\":{\"inData\":\"" + info.replace("\"", "\\\"") + "\",\"passwd\":\"88888888\"}}";
    }


   /* public Map<Object, Object> generatePayExInfoStr(String orderNo, String sessionId) {
        Map<Object, Object> payExInfo = new LinkedHashMap<>();
        payExInfo.put("sessionID", sessionId);

        TradeOrder order = tradeOrderMapper.selectOne(Wrappers.<TradeOrder>lambdaQuery().eq(TradeOrder::getOrderNo, orderNo));

        // head信息
        Map<Object, Object> payExchangeInfoHead = getPayExchangeInfoHead(order);
        payExInfo.put("payExchangeInfoHead", payExchangeInfoHead);

        // lists信息
        List<Map<Object, Object>> payExchangeInfoLists = getPayExchangeInfoLists(orderNo);
        payExInfo.put("payExchangeInfoLists", payExchangeInfoLists);

        long serviceTime = System.currentTimeMillis();
        payExInfo.put("serviceTime", String.valueOf(serviceTime));
        payExInfo.put("certNo", getCertNo());
        // 加签用的原始报文数据
        payExInfo.put("signValue", concatOriginSignVale(sessionId, payExchangeInfoHead, payExchangeInfoLists, serviceTime));
        return payExInfo;
    }*/


   /*
   public Map<Object, Object> getPayExchangeInfoHead(TradeOrder order) {
        OrderPaymentLine line = orderPaymentLineMapper.selectOne(Wrappers.<OrderPaymentLine>lambdaQuery()
                .eq(OrderPaymentLine::getOrderId, order.getId())
                .eq(OrderPaymentLine::getDelFlag, 0).eq(OrderPaymentLine::getPayType, 0));
        PayOrderDetailRequest request = new PayOrderDetailRequest();
        request.setPayNo(line.getOrderTransactionNumber());
        Result<PayOrderDetailResponse> result = paymentCFeignService.payOrderDetail(request);

        // 企业系统生成36位唯一序号（英文字母大写）
        String guid = UUID.randomUUID().toString().toUpperCase();
        Map<Object, Object> map = ViewObjUtils.getMap(16, true);
        map.put("guid", guid);
        // 支付报文含有xml,{}，这些特殊字符需要，URL编码
        map.put("initalRequest", getUrlEncode(result.getData().getUnifiedOrderRequest()));
        map.put("initalResponse", getUrlEncode(result.getData().getUnifiedOrderResponse()));
        map.put("ebpCode", "46121601BC");
        map.put("payCode", "440316T004");
        map.put("payTransactionId", line.getOrderTransactionNumber());
        map.put("totalAmount", order.getPaymentAmount());
        // https://baike.baidu.com/item/币制报关代码表
        map.put("currency", "142");
        map.put("verDept", "3");
        map.put("payType", "1");
        map.put("tradingTime", DateUtil.format(line.getPaymentDate(), "yyyyMMddHHmmss"));
        map.put("note", "跨境商品支付");
        return map;
    }
    */
}
