package cn.alphahub.eport.signature.config;

import cn.alphahub.multiple.email.config.EmailConfig;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * email config  properties
 *
 * @author weasley
 * @version 1.0
 * @date 2022/7/15
 */
@Data
@ConditionalOnProperty(prefix = "spring.mail", name = {"enable"}, havingValue = "true")
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    /**
     * 是否启用邮件通知功能
     */
    private Boolean enable = false;
    /**
     * 主收件人的邮箱
     */
    @Email(regexp = "^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$", message = "收件人邮箱格式不正确")
    @NotBlank(message = "主收件人邮箱不能为空")
    private String to;
    /**
     * 抄送箱邮（非必填）,抄送多个收件人箱邮用","隔开
     */
    private String cc;
    /**
     * 多邮件模板配置列表
     */
    private List<EmailConfig.EmailProperties> emailTemplates;
}
