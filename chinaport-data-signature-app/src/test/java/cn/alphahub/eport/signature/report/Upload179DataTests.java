package cn.alphahub.eport.signature.report;

import cn.alphahub.eport.signature.config.ChinaEportProperties;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import io.github.weasleyj.china.eport.sign.model.customs179.Customs179Request;
import io.github.weasleyj.china.eport.sign.util.GUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.Map;

import static cn.alphahub.dtt.plus.util.JacksonUtil.toJson;
import static cn.alphahub.eport.signature.core.CertificateHandler.SING_DATA_METHOD;

@Slf4j
@SpringBootTest
class Upload179DataTests {

    @Autowired
    private SignHandler signHandler;
    @Autowired
    private ChinaEportProperties eportProperties;

    /**
     * 返回结果示例
     * <pre>
     * {
     *     "code": "20004",
     *     "message": "企业实时数据获取验签证书未在服务系统注册",
     *     "total": 0,
     *     "serviceTime": 1688886074467
     * }
     * </pre>
     * <pre>
     * {
     *     "code": "10000",
     *     "message": "上传成功",
     *     "total": 0,
     *     "serviceTime": 1689166903449
     * }
     * </pre>
     */
    @Test
    @DisplayName("海关179号数据加签并上报")
    void upload() {
        String json1 = """
                {
                  "sessionID": "fe2374-8fnejf97-32839218",
                  "payExchangeInfoHead": {
                    "guid": "fe2374-8fnejf97-32839218",
                    "initalRequest": "<xml><appid>wxa3412c979c7c6fb7</appid><mch_id>1537923071</mch_id><nonce_str>20220214148382001542606848</nonce_str><sign>38198AD6D22E59B3CCC683D67D8BB402</sign><sign_type>MD5</sign_type><body>测试订单编号411312450000027161</body><attach><![CDATA[16dbeaa5-92d8-47f5-a0a8-813a4d45a5f9]]></attach><out_trade_no>20220214148382001542606848</out_trade_no><total_fee>1</total_fee><spbill_create_ip>10.0.12.208</spbill_create_ip><notify_url>https://boss-dev.fengyouhui.net/be/api/mall-payment-c/api/public/gateway/payment/notice/wx/pay/20220214148382001542606848</notify_url><trade_type>MWEB</trade_type><openid></openid></xml>",
                    "initalResponse": "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><result_code><![CDATA[SUCCESS]]></result_code><mch_id><![CDATA[1537923071]]></mch_id><appid><![CDATA[wxa3412c979c7c6fb7]]></appid><nonce_str><![CDATA[6SgxJ98xsr1nUnRG]]></nonce_str><sign><![CDATA[EB632B9C9F8BCB8027ACE9195878EE4F]]></sign><prepay_id><![CDATA[wx14105706563206ab24a00689936c800000]]></prepay_id><trade_type><![CDATA[MWEB]]></trade_type><mweb_url><![CDATA[https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx14105706563206ab24a00689936c800000&package=2518465012]]></mweb_url></xml>",
                    "ebpCode": "46121601BC",
                    "payCode": "1537923071",
                    "payTransactionId": "20220214148382001542606848",
                    "totalAmount": 0.01,
                    "currency": "502",
                    "verDept": "3",
                    "payType": "1",
                    "tradingTime": "20220214105714",
                    "note": "备注"
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
                      "recpAccount": "1537923071",
                      "recpCode": "1537923071",
                      "recpName": "河南风友汇实业有限公司商户"
                    }
                  ],
                  "serviceTime": "1533282603450"
                }
                """;

        String json2 = """
                {
                  "sessionID": "032C3F56-0EE6-4558-B548-6C7A3451F07D",
                  "payExchangeInfoHead": {
                    "guid": "E4766021-21AC-1AA2-21DD-E974DF93C11D",
                    "initalRequest": "<xml><appid><![CDATA[xwxwxwxwx112121]]></appid><mch_id>11111111</mch_id><body><![CDATA[测试测试]]></body><nonce_str><![CDATA[ashdgahgdhaghgliqwueyqu1]]></nonce_str><out_trade_no>202009231454212140210352</out_trade_no><total_fee>500</total_fee><spbill_create_ip><![CDATA[192.168.0.1]]></spbill_create_ip><notify_url><![CDATA[https://www.baidu.com]]></notify_url><openid><![CDATA[adasyduiyasdhjxzycua-Q]]></openid><trade_type><![CDATA[JSAPI]]></trade_type><sign><![CDATA[asd1sa56d4545wqe44wq5]]></sign></xml>",
                    "initalResponse": "<xml><appid><![CDATA[xwxwxwxwx112121]]></appid><bank_type><![CDATA[OTHERS]]></bank_type><cash_fee>500</cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id>11111111</mch_id><nonce_str><![CDATA[ashdgahgdhaghgliqwueyqu1]]></nonce_str><openid><![CDATA[adasyduiyasdhjxzycua-Q]]></openid><out_trade_no>202009231454212140210352</out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[asgdhasgdhasgdhgasgdhasgdh]]></sign><time_end>20200923145426</time_end><total_fee>500</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id>4200000681202009235085032319</transaction_id></xml>",
                    "ebpCode": "46016602EV",
                    "payCode": "1537923071",
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
                      "recpAccount": "46016602EV",
                      "recpCode": "46016602EV",
                      "recpName": "海南省荣誉进出口贸易有限公司"
                    }
                  ],
                  "serviceTime": "1601282210417"
                }
                """;
        Customs179Request customs179Request = JSONUtil.toBean(json2, Customs179Request.class);

        String guid = GUIDUtil.getGuid();
        customs179Request.setSessionID(guid);
        customs179Request.setServiceTime(String.valueOf(System.currentTimeMillis()));
        customs179Request.getPayExchangeInfoHead().setGuid(guid);

        // 将 server-url base64加密下，不适合直接公布到外网
        String REPORT_TEST_SERVER_URL_encode = "aHR0cHM6Ly9zd2FwcHRlc3Quc2luZ2xld2luZG93LmNuL2NlYjJncmFiL2dyYWIvcmVhbFRpbWVEYXRhVXBsb2Fk";
        String REPORT_PROD_SERVER_URL_encode = "aHR0cHM6Ly9jdXN0b21zLmNoaW5hcG9ydC5nb3YuY24vY2ViMmdyYWIvZ3JhYi9yZWFsVGltZURhdGFVcGxvYWQ=";

        String REPORT_TEST_SERVER_URL = Base64.decodeStr(REPORT_TEST_SERVER_URL_encode);
        String REPORT_PROD_SERVER_URL = Base64.decodeStr(REPORT_PROD_SERVER_URL_encode);

        // 加签
        String dataInfo179 = "\"sessionID\":\"" + customs179Request.getSessionID() + "\"||" +
                "\"payExchangeInfoHead\":\"" + toJson(customs179Request.getPayExchangeInfoHead()) + "\"||" +
                "\"payExchangeInfoLists\":\"" + toJson(customs179Request.getPayExchangeInfoLists()) + "\"||" +
                "\"serviceTime\":" + "\"" + customs179Request.getServiceTime() + "\"";

        UkeyRequest ukeyRequest = new UkeyRequest(SING_DATA_METHOD, new LinkedHashMap<>() {{
            put("inData", dataInfo179);
            put("passwd", "88888888");
        }});
        String signParams = toJson(ukeyRequest);
        SignRequest request = new SignRequest(dataInfo179);
        SignResult result = signHandler.sign(request, signParams);

        customs179Request.setCertNo(result.getCertNo());
        customs179Request.setSignValue(result.getSignatureValue());

        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("payExInfoStr", toJson(customs179Request));
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

}
