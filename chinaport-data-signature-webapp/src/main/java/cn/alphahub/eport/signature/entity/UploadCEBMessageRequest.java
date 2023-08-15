package cn.alphahub.eport.signature.entity;

import cn.alphahub.eport.signature.base.enums.RequestDataType;
import cn.hutool.json.JSONUtil;
import io.github.weasleyj.china.eport.sign.constants.MessageType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * 消息类型
     */
    @NotNull(message = "消息类型不能为空")
    private MessageType messageType;
    /**
     * 报文的数据类型
     */
    @NotNull(message = "请求报文的数据类型不能为空")
    private RequestDataType dataType;
    /**
     * 进口单、出口单的底层数据模型
     * <ul>
     * 以CEB311Message数据类型为例
     * <li>
     * <p>JSON示例</p>
     * <pre><code>
     * {"order":{"orderHead":{"guid":"AFNC7T-WEASLEY-20230713095517-N04JEE","appType":"1","appTime":"20230704181028","appStatus":"2","orderType":"I","orderNo":"T_C5051511332138160010","ebpCode":"4601630004","ebpName":"海南省荣誉进出口贸易有限公司","ebcCode":"4601630004","ebcName":"海南省荣誉进出口贸易有限公司","goodsValue":"0.01","freight":"0","discount":"0","taxTotal":"0","acturalPaid":"0.01","currency":"142","buyerRegNo":"4","buyerName":"袁晓雨","buyerTelephone":"13701727375","buyerIdType":"1","buyerIdNumber":"130435200009241538","consignee":"袁晓雨","consigneeTelephone":"13701727375","consigneeAddress":"北京北京市东城区","note":"test"},"orderList":[{"gnum":1,"itemNo":"1","itemName":"LANNA兰纳","gmodel":"10片/包","itemDescribe":"","barCode":"1","unit":"011","qty":"1","price":"1","totalPrice":"1","currency":"142","country":"136","note":"test"}]},"guid":"AFNC7T-WEASLEY-20230713095517-N04JEE","version":"1.0","baseTransfer":{"copCode":"4601630004","copName":"海南省荣誉进出口贸易有限公司","dxpMode":"DXP","dxpId":"DXPENT0000530815","note":"test"}}
     * </code></pre>
     * </li>
     * <li>
     * <p>XML示例: 不带ds:Signature节点的CEBXxxMessage.xml原文;<a href='http://tool.qdhuaxun.cn/ceb/CEB311Message.xml'>待加签xml报文样例</a></p>
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
