package cn.alphahub.eport.signature.core.web;

import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.hutool.json.JSONUtil;
import io.github.weasleyj.china.eport.sign.model.customs179.Customs179Request;
import io.github.weasleyj.china.eport.sign.util.GUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.Map;

import static cn.alphahub.dtt.plus.util.JacksonUtil.toJson;
import static cn.alphahub.eport.signature.core.CertificateHandler.SING_DATA_METHOD;

@Slf4j
@SpringBootTest
class EportCustoms179HttpClientTest {

    @Autowired
    SignHandler signHandler;
    @Autowired
    EportCustoms179HttpClient eportCustoms179HttpClient;

    @Test
    void report179Data() {
        String json = """
                {
                  "sessionID": "fe2374-8fnejf97-32839218",
                  "payExchangeInfoHead": {
                    "guid": "fe2374-8fnejf97-32839218",
                    "initalRequest": "<xml><appid>wxa3412c979c7c6fb7</appid><mch_id>1537923071</mch_id><nonce_str>20220214148382001542606848</nonce_str><sign>38198AD6D22E59B3CCC683D67D8BB402</sign><sign_type>MD5</sign_type><body>测试订单编号411312450000027161</body><attach><![CDATA[16dbeaa5-92d8-47f5-a0a8-813a4d45a5f9]]></attach><out_trade_no>20220214148382001542606848</out_trade_no><total_fee>1</total_fee><spbill_create_ip>10.0.12.208</spbill_create_ip><notify_url>https://boss-dev.fengyouhui.net/be/api/mall-payment-c/api/public/gateway/payment/notice/wx/pay/20220214148382001542606848</notify_url><trade_type>MWEB</trade_type><openid></openid></xml>",
                    "initalResponse": "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><result_code><![CDATA[SUCCESS]]></result_code><mch_id><![CDATA[1537923071]]></mch_id><appid><![CDATA[wxa3412c979c7c6fb7]]></appid><nonce_str><![CDATA[6SgxJ98xsr1nUnRG]]></nonce_str><sign><![CDATA[EB632B9C9F8BCB8027ACE9195878EE4F]]></sign><prepay_id><![CDATA[wx14105706563206ab24a00689936c800000]]></prepay_id><trade_type><![CDATA[MWEB]]></trade_type><mweb_url><![CDATA[https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx14105706563206ab24a00689936c800000&package=2518465012]]></mweb_url></xml>",
                    "ebpCode": "请替换",
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
                      "recpAccount": "请替换",
                      "recpCode": "请替换",
                      "recpName": "请替换"
                    }
                  ],
                  "serviceTime": "1533282603450"
                }
                """;
        Customs179Request customs179Request = JSONUtil.toBean(json, Customs179Request.class);

        String guid = GUIDUtil.getGuid();
        customs179Request.setSessionID(guid);
        customs179Request.setServiceTime(String.valueOf(System.currentTimeMillis()));
        customs179Request.getPayExchangeInfoHead().setGuid(guid);

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
        log.info("begin report，request data is ：{}", toJson(paramMap));
        eportCustoms179HttpClient.report179Data(paramMap).subscribe(capture179DataResponse -> {
            Assertions.assertNotNull(capture179DataResponse, "海关 179 数据返回不为空");
            System.out.println(JSONUtil.toJsonPrettyStr(capture179DataResponse));
        });
    }
}
