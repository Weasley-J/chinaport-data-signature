package cn.alphahub.eport.signature.entity;

import cn.alphahub.eport.signature.base.enums.RequestDataType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import o.github.weasleyj.china.eport.sign.constants.MessageType;

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
public class UploadCEBMessageRequest implements Serializable {
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
     */
    @NotBlank(message = "进口单、出口单的底层数据模型不能为空")
    private String cebMessage;

    public String getCebMessage() {
        return cebMessage.trim();
    }
}
