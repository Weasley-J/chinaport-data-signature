package cn.alphahub.eport.signature.entity;

import io.github.weasleyj.china.eport.sign.model.customs179.PayExchangeInfoBody;
import io.github.weasleyj.china.eport.sign.model.customs179.PayExchangeInfoHead;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 海关179数据抓取
 *
 * @author weasley
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Capture179DataRequest implements Serializable {
    /**
     * 海关发起请求时，平台接收的会话ID
     */
    @NotBlank
    private String sessionID;
    /**
     * 支付原始数据表头
     */
    @NotNull
    private PayExchangeInfoHead payExchangeInfoHead;
    /**
     * 支付原始数据表体
     */
    @NotEmpty
    private List<PayExchangeInfoBody> payExchangeInfoLists;
    /**
     * 返回时的系统时间
     */
    @NotBlank
    private String serviceTime;
}
