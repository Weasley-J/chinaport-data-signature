package cn.alphahub.eport.signature.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * CEBXxxMessage加签请求入参
 *
 * @author weasley
 * @version 1.0
 * @date 2022/2/13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SignRequest implements Serializable {
    /**
     * 唯一id, 用来区分是哪一次发送的消息，默认值=1，从1开始，{@code int} 最大, 2<sup>31</sup>-1.
     */
    @Nullable
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private Integer id = 1;
    /**
     * 不带ds:Signature节点的CEbXXXMessage.xml原文
     */
    @NotBlank(message = "不带<ds:Signature></ds:Signature>节点的CEbXXXMessage.xml原文不能为空")
    private String sourceXml;

    @Nullable
    public Integer getId() {
        return (id == null || id <= 0) ? 1 : id;
    }
}
