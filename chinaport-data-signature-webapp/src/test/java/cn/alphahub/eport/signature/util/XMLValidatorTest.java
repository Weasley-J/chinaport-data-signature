package cn.alphahub.eport.signature.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static cn.alphahub.eport.signature.support.XMLValidator.isValidXML;

/**
 * XML校验器
 *
 * @author weasley
 * @since 1.1.0
 */
class XMLValidatorTest {

    @Test
    @DisplayName("校验字符串字符串是不是XML")
    void testValidXML() {
        boolean validXML = isValidXML("""
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>org.springframework.cloud</groupId>
                                <artifactId>spring-cloud-dependencies</artifactId>
                                <version>${spring-cloud.version}</version>
                                <type>pom</type>
                                <scope>import</scope>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
                """);
        Assert.isTrue(validXML, "XML is valid");

        String xmlString = "<root><element>Value</element></root>";
        boolean isValid = isValidXML(xmlString);
        Assert.isTrue(isValid, "XML is valid");

        xmlString = """
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

        boolean b = isValidXML(xmlString);
        Assert.isTrue(!b, "XML is not valid");
    }
}
