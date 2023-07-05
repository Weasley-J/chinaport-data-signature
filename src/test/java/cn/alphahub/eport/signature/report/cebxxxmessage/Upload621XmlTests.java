package cn.alphahub.eport.signature.report.cebxxxmessage;

import cn.alphahub.dtt.plus.util.JacksonUtil;
import cn.alphahub.eport.signature.report.cebxxxmessage.constants.MessageType;
import cn.alphahub.eport.signature.report.cebxxxmessage.entity.CEB621Message;
import cn.alphahub.eport.signature.report.cebxxxmessage.util.GUIDUtil;
import cn.alphahub.eport.signature.report.cebxxxmessage.util.JAXBUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 621 进口单 xml 上报测试
 *
 * @author weasley
 * @version 1.0.0
 */
@Slf4j
@SpringBootTest
class Upload621XmlTests {

    @Autowired
    ChinaEportReportClient chinaEportReportClient;

    @Test
    @DisplayName("621 进口单 xml 上报测试")
    void push() {
        String sourceXml = """
                <ceb:CEB621Message guid="CEB621_HNZB_FXJK_20220209104827_0054" version="v1.0" xmlns:ceb="http://www.chinaport.gov.cn/ceb">
                    <ceb:Inventory>
                        <ceb:InventoryHead>
                            <ceb:guid>CEB621_HNZB_FXJK_20220209104827_0055</ceb:guid>
                            <ceb:appType>1</ceb:appType>
                            <ceb:appTime>20220209104827</ceb:appTime>
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
                            <ceb:declTime>20220209</ceb:declTime>
                            <ceb:customsCode>4601</ceb:customsCode>
                            <ceb:portCode>4601</ceb:portCode>
                            <ceb:ieDate>20220209</ceb:ieDate>
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
                            <ceb:gmodel>4|3|乳清蛋白(牛奶)65%,胶囊壳(羟苯基甲基纤维素)21%,硫酸亚铁1.2%,酸度调节剂2%,填充剂
                                10.8%|30粒/盒|无中文名称/ACTIVE IRON
                            </ceb:gmodel>
                            <ceb:country>306</ceb:country>
                            <ceb:currency>142</ceb:currency>
                            <ceb:qty>1</ceb:qty>
                            <ceb:unit>140</ceb:unit>
                            <ceb:qty1>0.06</ceb:qty1>
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
                    <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
                        <ds:SignedInfo>
                            <ds:CanonicalizationMethod Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"/>
                            <ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#rsa-sha1"/>
                            <ds:Reference URI="">
                                <ds:Transforms>
                                    <ds:Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/>
                                </ds:Transforms>
                                <ds:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
                                <ds:DigestValue>qg6+PnUxyHolo1QeJ6KEmBZjD8Q=</ds:DigestValue>
                            </ds:Reference>
                        </ds:SignedInfo>
                        <ds:SignatureValue>
                            EsTnf2YMmyQjnlNuu9+sDVQniiUtpbEfjg229AY18PDqOWKtajL3Ae0MA2opeaFt9NoE9nU7Qv0bAGZa2+KW2LiGwsNiyin32kt0LplwVHNxyTezUV9t6xw4uMGsNO4D7SUgZxUAyeYi6uRUrgoSRMn4VFZigUY+q0H1+LCuIsE=
                        </ds:SignatureValue>
                        <ds:KeyInfo>
                            <ds:KeyName>01691fe9</ds:KeyName>
                            <ds:X509Data>
                                <ds:X509Certificate>
                                    MIIE+TCCBGKgAwIBAgIEAWkf6TANBgkqhkiG9w0BAQUFADB8MQswCQYDVQQGEwJjbjEVMBMGA1UECh4MTi1W/XU1W1BT41y4MRUwEwYDVQQLHgyLwU5me6F0Bk4tX8MxDTALBgNVBAgeBFMXTqwxITAfBgNVBAMeGE4tVv11NVtQThpSoYvBTmZ7oXQGTi1fwzENMAsGA1UEBx4EUxdOrDAeFw0yMTA1MzEwMDAwMDBaFw0zMTA1MzEwMDAwMDBaMBwxDTALBgNVBAMeBH8qdDMxCzAJBgNVBBIeAgAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDt9T7JOQikKKL7oY7RWbRMp7y2VsrXIbFzSDh5DZQPimduUijZzZlK6AkZMDYJBM2/IJGI0QjWjBJVZ8hEUkgJb4UOOBCBM+cCGFCNY5LX/mAo5BoexgG4Kdr0TwxAQ+s3H85fCU81ROgqAS05/IzOR7eDSEYYT9CRmNYXIRTPVwIDAQABo4IC5jCCAuIwCwYDVR0PBAQDAgbAMAkGA1UdEwQCMAAwgacGA1UdIwSBnzCBnIAU+XWjeEULQmjCBFOr68NOPlR4dGChgYCkfjB8MQswCQYDVQQGEwJjbjEVMBMGA1UECh4MTi1W/XU1W1BT41y4MRUwEwYDVQQLHgyLwU5me6F0Bk4tX8MxDTALBgNVBAgeBFMXTqwxITAfBgNVBAMeGE4tVv11NVtQThpSoYvBTmZ7oXQGTi1fwzENMAsGA1UEBx4EUxdOrIIBADAdBgNVHQ4EFgQU1eAz50lwZ6gidtkJNPIr7O0jKvUwQgYDVR0gBDswOTA3BgYrgQcBAQIwLTArBggrBgEFBQcCARYfaHR0cDovL2Nwcy5jaGluYXBvcnQuZ292LmNuL0NQUzByBgNVHR8EazBpMDCgLqAshipsZGFwOi8vbGRhcC5jaGluYXBvcnQuZ292LmNuOjM4OS8wMDAtMS5jcmwwNaAzoDGGL2h0dHA6Ly9sZGFwLmNoaW5hcG9ydC5nb3YuY246ODA4OC9kemthMDAwLTEuY3JsMG0GCCsGAQUFBwEBBGEwXzAuBggrBgEFBQcwAYYiaHR0cDovL29jc3AuY2hpbmFwb3J0Lmdvdi5jbjo4ODAwLzAtBggrBgEFBQcwAYYhaHR0cDovL29jc3AuY2hpbmFwb3J0Lmdvdi5jbjo4MDg4MCoGCisGAQQBqUNkBQEEHBYauqPEz9DHtLS7pcGqzfjSvdKp09DP3rmry74wEgYKKwYBBAGpQ2QFAwQEFgIwMTAiBgorBgEEAalDZAUIBBQWEjMxMDEwMjE5ODAwNzI0MzIxNDAdBgorBgEEAalDZAUJBA8WDUpKMEc5MDAxNTkyNzIwGQYKKwYBBAGpQ2QFCwQLFglNQTVUTFVBNTMwEgYKKwYBBAGpQ2QFDAQEFgIwMDASBgorBgEEAalDZAIEBAQWAjE0MBIGCisGAQQBqUNkAgEEBBYCMTIwDQYJKoZIhvcNAQEFBQADgYEAqSuOMxAzM4bXvdlDcE6fODsCvQMFKctlA+LCllFQwl58HaBmcWx4T/ddKF9LBYc8A986LlcUw6Mkwxraj2WO+meXdDzRLEO8t3gyZk7tYp5aneV4zGYUsphwyMdLt8N8o4kVgg16bQy43XgA1jRMv8nvhb908IqQQBxwv0SIuXU=
                                </ds:X509Certificate>
                            </ds:X509Data>
                        </ds:KeyInfo>
                    </ds:Signature>
                </ceb:CEB621Message>
                """;
        CEB621Message ceb621Message = JAXBUtil.convertToObj(sourceXml, CEB621Message.class);
        assert ceb621Message != null;
        ceb621Message.setGuid(GUIDUtil.getDayIncrCode(GUIDUtil.INVENTORY, GUIDUtil.CEB621MESSAGE, 4));
        String guid = GUIDUtil.getDayIncrCode(GUIDUtil.ORDERPUSH, GUIDUtil.CEB311MESSAGE, 4);
        ceb621Message.setGuid(guid);
        ceb621Message.getInventory().getInventoryHead().setGuid(guid);
        ceb621Message.setVersion("1.0");
        ceb621Message.setBaseTransfer(chinaEportReportClient.assembleBaseTransfer()); //参数需要替换成自己企业的

        /*
        Inventory inventory = new Inventory();
        InventoryHead headReq = getInventoryHeadReq(order,  request.getReceiverIdCard(), request.getInvoiceNo(),request.getPayNo());
        ArrayList<InventoryList> listReq = getInventoryListReq(item, headReq);
        inventory.setInventoryHead(headReq);
        inventory.setInventoryListList(listReq);
        ceb621Message.setInventory(inventory);
        */

        System.out.println(JacksonUtil.toJson(ceb621Message));
        String xml = JAXBUtil.convertToXml(ceb621Message);

        chinaEportReportClient.push(ceb621Message, MessageType.CEB621Message);
    }
}
