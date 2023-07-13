package cn.alphahub.eport.signature.entity;

import cn.alphahub.eport.signature.base.enums.RequestDataType;
import cn.hutool.json.JSONUtil;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import o.github.weasleyj.china.eport.sign.constants.MessageType;

import java.io.Serial;
import java.io.Serializable;

/**
 * 上报进口单、出口单入参
 *
 * @author weasley
 * @version 1.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings({"all"})
public class UploadCEBMessageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1905122041950251207L;
    /**
     * 消息类型: CEB311Message|CEB621Message|...
     */
    @NotNull(message = "消息类型不能为空")
    private MessageType messageType;
    /**
     * 报文的数据类型: JSON, XML
     */
    @NotNull(message = "请求报文的数据类型不能为空")
    private RequestDataType dataType;
    /**
     * 进口单、出口单的底层数据模型
     *
     * @apiNote <ul>
     * 以CEB311Message数据类型为例
     * <li>
     * <p>JSON示例</p>
     * <pre><code>
     * {"order":{"orderHead":{"guid":"AFNC7T-WEASLEY-20230713095517-N04JEE","appType":"1","appTime":"20230704181028","appStatus":"2","orderType":"I","orderNo":"T_C5051511332138160010","ebpCode":"4601630004","ebpName":"海南省荣誉进出口贸易有限公司","ebcCode":"4601630004","ebcName":"海南省荣誉进出口贸易有限公司","goodsValue":"0.01","freight":"0","discount":"0","taxTotal":"0","acturalPaid":"0.01","currency":"142","buyerRegNo":"4","buyerName":"袁晓雨","buyerTelephone":"13701727375","buyerIdType":"1","buyerIdNumber":"130435200009241538","consignee":"袁晓雨","consigneeTelephone":"13701727375","consigneeAddress":"北京北京市东城区","note":"test"},"orderList":[{"gnum":1,"itemNo":"1","itemName":"LANNA兰纳","gmodel":"10片/包","itemDescribe":"","barCode":"1","unit":"011","qty":"1","price":"1","totalPrice":"1","currency":"142","country":"136","note":"test"}]},"guid":"AFNC7T-WEASLEY-20230713095517-N04JEE","version":"1.0","baseTransfer":{"copCode":"4601630004","copName":"海南省荣誉进出口贸易有限公司","dxpMode":"DXP","dxpId":"DXPENT0000530815","note":"test"}}
     * </code></pre>
     * </li>
     * <li>
     * <p>XML示例</p>
     * <pre><code>
     * &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
     * &lt;ceb:CEB311Message guid=&quot;tCEB311_HNZB_HNFX_20230707223752_003&quot; version=&quot;1.0&quot; xmlns:ceb=&quot;http://www.chinaport.gov.cn/ceb&quot; xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;&gt;
     *     &lt;ceb:Order&gt;
     *         &lt;ceb:OrderHead&gt;
     *             &lt;ceb:guid&gt;tCEB311_HNZB_HNFX_20230707223752_001&lt;/ceb:guid&gt;
     *             &lt;ceb:appType&gt;1&lt;/ceb:appType&gt;
     *             &lt;ceb:appTime&gt;20230704181028&lt;/ceb:appTime&gt;
     *             &lt;ceb:appStatus&gt;2&lt;/ceb:appStatus&gt;
     *             &lt;ceb:orderType&gt;I&lt;/ceb:orderType&gt;
     *             &lt;ceb:orderNo&gt;T_C5051511332138160010&lt;/ceb:orderNo&gt;
     *             &lt;ceb:ebpCode&gt;4601630004&lt;/ceb:ebpCode&gt;
     *             &lt;ceb:ebpName&gt;海南省荣誉进出口贸易有限公司&lt;/ceb:ebpName&gt;
     *             &lt;ceb:ebcCode&gt;4601630004&lt;/ceb:ebcCode&gt;
     *             &lt;ceb:ebcName&gt;海南省荣誉进出口贸易有限公司&lt;/ceb:ebcName&gt;
     *             &lt;ceb:goodsValue&gt;0.01&lt;/ceb:goodsValue&gt;
     *             &lt;ceb:freight&gt;0&lt;/ceb:freight&gt;
     *             &lt;ceb:discount&gt;0&lt;/ceb:discount&gt;
     *             &lt;ceb:taxTotal&gt;0&lt;/ceb:taxTotal&gt;
     *             &lt;ceb:acturalPaid&gt;0.01&lt;/ceb:acturalPaid&gt;
     *             &lt;ceb:currency&gt;142&lt;/ceb:currency&gt;
     *             &lt;ceb:buyerRegNo&gt;4&lt;/ceb:buyerRegNo&gt;
     *             &lt;ceb:buyerName&gt;袁晓雨&lt;/ceb:buyerName&gt;
     *             &lt;ceb:buyerTelephone&gt;13701727375&lt;/ceb:buyerTelephone&gt;
     *             &lt;ceb:buyerIdType&gt;1&lt;/ceb:buyerIdType&gt;
     *             &lt;ceb:buyerIdNumber&gt;130435200009241538&lt;/ceb:buyerIdNumber&gt;
     *             &lt;ceb:consignee&gt;袁晓雨&lt;/ceb:consignee&gt;
     *             &lt;ceb:consigneeTelephone&gt;13701727375&lt;/ceb:consigneeTelephone&gt;
     *             &lt;ceb:consigneeAddress&gt;北京北京市东城区&lt;/ceb:consigneeAddress&gt;
     *             &lt;ceb:note&gt;test&lt;/ceb:note&gt;
     *         &lt;/ceb:OrderHead&gt;
     *         &lt;ceb:OrderList&gt;
     *             &lt;ceb:gnum&gt;1&lt;/ceb:gnum&gt;
     *             &lt;ceb:itemNo&gt;1&lt;/ceb:itemNo&gt;
     *             &lt;ceb:itemName&gt;LANNA兰纳&lt;/ceb:itemName&gt;
     *             &lt;ceb:gmodel&gt;10片/包&lt;/ceb:gmodel&gt;
     *             &lt;ceb:itemDescribe&gt;&lt;/ceb:itemDescribe&gt;
     *             &lt;ceb:barCode&gt;1&lt;/ceb:barCode&gt;
     *             &lt;ceb:unit&gt;011&lt;/ceb:unit&gt;
     *             &lt;ceb:qty&gt;1&lt;/ceb:qty&gt;
     *             &lt;ceb:price&gt;1&lt;/ceb:price&gt;
     *             &lt;ceb:totalPrice&gt;1&lt;/ceb:totalPrice&gt;
     *             &lt;ceb:currency&gt;142&lt;/ceb:currency&gt;
     *             &lt;ceb:country&gt;136&lt;/ceb:country&gt;
     *             &lt;ceb:note&gt;test&lt;/ceb:note&gt;
     *         &lt;/ceb:OrderList&gt;
     *     &lt;/ceb:Order&gt;
     *     &lt;ceb:BaseTransfer&gt;
     *         &lt;ceb:copCode&gt;4601630004&lt;/ceb:copCode&gt;
     *         &lt;ceb:copName&gt;海南省荣誉进出口贸易有限公司&lt;/ceb:copName&gt;
     *         &lt;ceb:dxpMode&gt;DXP&lt;/ceb:dxpMode&gt;
     *         &lt;ceb:dxpId&gt;DXPENT0000530815&lt;/ceb:dxpId&gt;
     *         &lt;ceb:note&gt;test&lt;/ceb:note&gt;
     *     &lt;/ceb:BaseTransfer&gt;
     * &lt;/ceb:CEB311Message&gt;
     * </code></pre>
     * </li>
     * </ul>
     */
    @NotNull(message = "进口单、出口单的底层数据模型不能为空")
    private Object cebMessage;

    public Object getCebMessage() {
        return switch (dataType) {
            case JSON -> JSONUtil.toJsonStr(cebMessage);
            case XML -> cebMessage;
        };
    }
}
