package cn.alphahub.eport.signature.feign;

import cn.alphahub.dtt.plus.util.JacksonUtil;
import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.config.UkeyAccessClientProperties.Command;
import cn.alphahub.eport.signature.entity.Capture179DataRequest;
import cn.alphahub.eport.signature.entity.Capture179DataResponse;
import cn.alphahub.eport.signature.entity.ConsoleOutput;
import cn.alphahub.eport.signature.entity.ThirdAbstractResponse;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static com.alibaba.nacos.common.utils.JacksonUtils.toJson;

@Slf4j
@SpringBootTest
class ChinaEportSignatureFeignClientTest {

    @Autowired
    ChinaEportSignatureFeignClient eportSignatureFeignClient;

    @BeforeEach
    void setUp() {
    }

    @Test
    void downloadX509Certificate() {
    }

    @Test
    void signature() {
    }

    @Test
    void uploadCEBMessage() {
    }

    @Test
    void capture179Data() {
        String json = """
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
        Capture179DataRequest customs179Request = JSONUtil.toBean(json, Capture179DataRequest.class);
        Result<ThirdAbstractResponse<Map<String, Object>, String, Capture179DataResponse>> result = eportSignatureFeignClient.capture179Data(customs179Request);
        log.info(JacksonUtil.toPrettyJson(result));
    }

    @Test
    void endpoint() {
        Result<ConsoleOutput> endpoint = eportSignatureFeignClient.endpoint(Command.RESTART);
        log.info(toJson(endpoint));
    }
}
