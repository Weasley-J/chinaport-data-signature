package cn.alphahub.eport.signature.config;

import cn.alphahub.multiple.email.config.EmailConfig;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static cn.alphahub.dtt.plus.util.StringUtils.isBlank;

/**
 * email config  properties
 *
 * @author weasley
 * @version 1.0
 * @date 2022/7/15
 */
@Data
@Slf4j
@ConditionalOnProperty(prefix = "spring.mail", name = {"enable"}, havingValue = "true")
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    private static final String REGEXP = "^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$";
    /**
     * 是否启用邮件通知功能
     */
    private Boolean enable = false;
    /**
     * 主收件人的邮箱
     */
    @Email(regexp = REGEXP, message = "收件人邮箱格式不正确")
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

    /**
     * 获取抄送邮箱列表
     */
    public String[] getCcEmails() {
        if (isBlank(cc)) {
            return new String[0];
        }
        String[] ccEmails = cc.split(",");
        Set<String> emails = new LinkedHashSet<>();
        for (String ccEmail : ccEmails) {
            String trimmed = ccEmail.trim();
            if (!trimmed.matches(REGEXP)) {
                log.error("The format of the CC email is incorrect: {}", trimmed);
            } else {
                emails.add(trimmed);
            }
        }
        return emails.toArray(new String[0]);
    }
}
