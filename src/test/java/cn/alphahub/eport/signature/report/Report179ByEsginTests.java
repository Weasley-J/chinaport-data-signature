package cn.alphahub.eport.signature.report;


import cn.alphahub.eport.signature.after202207.SignConstant;
import cn.alphahub.eport.signature.basic.domain.Result;
import cn.alphahub.eport.signature.controller.rpc.EportSignController;
import cn.alphahub.eport.signature.core.CertificateHandler;
import cn.alphahub.eport.signature.core.SignHandler;
import cn.alphahub.eport.signature.entity.SignRequest;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.alphahub.eport.signature.entity.UkeyRequest;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
public class Report179ByEsginTests {
    @Autowired
    private SignHandler signHandler;

    @Autowired
    private EportSignController signController;

    @Test
    public void report() {
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

        String REPORT_TEST_SERVER_URL = "https://swapptest.singlewindow.cn/ceb2grab/grab/realTimeDataUpload";
        String REPORT_PROD_SERVER_URL = "https://customs.chinaport.gov.cn/ceb2grab/grab/realTimeDataUpload";

        SignRequest request = new SignRequest(SignConstant.sign179);
        Result<SignResult> signature = signController.signature(request);
        Map<String, Object> paramMap = new LinkedHashMap<>();

        report179Request.setCertNo(signature.getData().getCertNo());
        report179Request.setSignValue(signature.getData().getSignatureValue());

        paramMap.put("payExInfoStr", JSONUtil.toJsonStr(report179Request));
        log.info("begin report，request data is ：{}", JSONUtil.toJsonStr(paramMap));
        try {
            HttpResponse response = HttpUtil.createPost(REPORT_PROD_SERVER_URL)
                    .form(paramMap)
                    .header("Content-Type", "application/x-www-form-urlencoded", Boolean.FALSE)
                    .timeout(5 * 1000)
                    .execute();
            System.err.println(response.body());
        } catch (Exception e) {
            log.error("report failure!", e);
        }
    }
}
