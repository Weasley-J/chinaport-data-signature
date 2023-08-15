package cn.alphahub.eport.signature.report.export;

import cn.alphahub.dtt.plus.util.JacksonUtil;
import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.entity.SignResult;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.weasleyj.china.eport.sign.model.cebmessage.export.CEB303Message;
import io.github.weasleyj.china.eport.sign.util.JAXBUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

/**
 * 621 进口单 xml 上报测试
 *
 * @author weasley
 * @version 1.0.0
 */
@Slf4j
@SpringBootTest
class Upload303XmlTests {

    @Test
    @DisplayName("303进口单加签测试")
    void sign() {
        final String data = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <ceb:CEB303Message xmlns:ceb="http://www.chinaport.gov.cn/ceb" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" guid="CEB303_ZH_GZ_20230309112945042_67622" version="1.0">
                  <ceb:Order>
                    <ceb:OrderHead>
                      <ceb:guid>CEB303_ZH_GZ_20230309112945042_67622</ceb:guid>
                      <ceb:appType>1</ceb:appType>
                      <ceb:appTime>20230309112945</ceb:appTime>
                      <ceb:appStatus>2</ceb:appStatus>
                      <ceb:orderType>E</ceb:orderType>
                      <ceb:orderNo>SGFGP0003129133YKKA7</ceb:orderNo>
                      <ceb:ebpCode>44306650K2</ceb:ebpCode>
                      <ceb:ebpName>广州市汇客物流有限公司</ceb:ebpName>
                      <ceb:ebcCode>44306650K2</ceb:ebcCode>
                      <ceb:ebcName>广州市汇客物流有限公司</ceb:ebcName>
                      <ceb:goodsValue>13.89000</ceb:goodsValue>
                      <ceb:freight>0.00000</ceb:freight>
                      <ceb:currency>142</ceb:currency>
                      <ceb:note>希音</ceb:note>
                    </ceb:OrderHead>
                    <ceb:OrderList>
                      <ceb:gnum>1</ceb:gnum>
                      <ceb:itemNo>se2209024331104035</ceb:itemNo>
                      <ceb:itemName>手机支架</ceb:itemName>
                      <ceb:barCode>无</ceb:barCode>
                      <ceb:unit>011</ceb:unit>
                      <ceb:currency>142</ceb:currency>
                      <ceb:qty>1.00000</ceb:qty>
                      <ceb:price>13.89000</ceb:price>
                      <ceb:totalPrice>13.89000</ceb:totalPrice>
                      <ceb:note>希音</ceb:note>
                    </ceb:OrderList>
                  </ceb:Order>
                  <ceb:BaseTransfer>
                    <ceb:copCode>44306650K2</ceb:copCode>
                    <ceb:copName>广州市汇客物流有限公司</ceb:copName>
                    <ceb:dxpMode>DXP</ceb:dxpMode>
                    <ceb:dxpId>向中国电子口岸数据中心申请数据交换平台的用户编号</ceb:dxpId>
                  </ceb:BaseTransfer>
                </ceb:CEB303Message>
                """;
        HttpResponse response = HttpUtil.createPost("http://localhost:8080/rpc/eport/signature")
                .contentType(ContentType.JSON.getValue())
                .header("x-auth-token-eport-sign", "DefaultAuthToken")
                .body(JSONUtil.toJsonStr(new HashMap<String, Object>() {{
                    put("id", 1);
                    put("data", data);
                }}))
                .execute();
        System.err.println(response.body());
        Result<SignResult> result = JacksonUtil.readValue(response.body(), new TypeReference<Result<SignResult>>() {
        });
        System.err.println("\n" + result.getData().getSignatureNode());
    }


    @Test
    void xmlToCEB303Message() {
        String xml = """
                <?xml version="1.0" encoding="utf-8" standalone="no"?>
                <ceb:CEB303Message xmlns:ceb="http://www.chinaport.gov.cn/ceb" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" guid="CEB303_ZH_GZ_20230309112945042_67615" version="1.0">
                  <ceb:Order>
                    <ceb:OrderHead>
                      <ceb:guid>CEB303_ZH_GZ_20230309112945042_67615</ceb:guid>
                      <ceb:appType>1</ceb:appType>
                      <ceb:appTime>20230309112945</ceb:appTime>
                      <ceb:appStatus>2</ceb:appStatus>
                      <ceb:orderType>E</ceb:orderType>
                      <ceb:orderNo>SGFGP0003129133YKK5</ceb:orderNo>
                      <ceb:ebpCode>44306650K2</ceb:ebpCode>
                      <ceb:ebpName>广州市汇客物流有限公司</ceb:ebpName>
                      <ceb:ebcCode>44306650K2</ceb:ebcCode>
                      <ceb:ebcName>广州市汇客物流有限公司</ceb:ebcName>
                      <ceb:goodsValue>13.89000</ceb:goodsValue>
                      <ceb:freight>0.00000</ceb:freight>
                      <ceb:currency>142</ceb:currency>
                      <ceb:note>希音</ceb:note>
                    </ceb:OrderHead>
                    <ceb:OrderList>
                      <ceb:gnum>1</ceb:gnum>
                      <ceb:itemNo>se2209024331104035</ceb:itemNo>
                      <ceb:itemName>手机支架</ceb:itemName>
                      <ceb:barCode>无</ceb:barCode>
                      <ceb:unit>011</ceb:unit>
                      <ceb:currency>142</ceb:currency>
                      <ceb:qty>1.00000</ceb:qty>
                      <ceb:price>13.89000</ceb:price>
                      <ceb:totalPrice>13.89000</ceb:totalPrice>
                      <ceb:note>test</ceb:note>
                    </ceb:OrderList>
                  </ceb:Order>
                  <ceb:BaseTransfer>
                    <ceb:copCode>44306650K2</ceb:copCode>
                    <ceb:copName>广州市汇客物流有限公司</ceb:copName>
                    <ceb:dxpMode>DXP</ceb:dxpMode>
                    <ceb:dxpId>向中国电子口岸数据中心申请数据交换平台的用户编号</ceb:dxpId>
                  </ceb:BaseTransfer>
                </ceb:CEB303Message>
                """;
        CEB303Message ceb303Message = JAXBUtil.toBean(xml, CEB303Message.class);
        System.err.println(JSONUtil.toJsonPrettyStr(ceb303Message) + "\n");
        String ceb303MessageXml = JAXBUtil.toXml(ceb303Message);
        System.out.println(ceb303MessageXml);
    }
}
