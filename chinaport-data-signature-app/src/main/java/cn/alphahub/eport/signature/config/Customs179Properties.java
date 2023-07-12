package cn.alphahub.eport.signature.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * 海关 179 数据上报配置元数据
 *
 * @author weasley
 * @version 1.1.0
 * @apiNote 如果接口没有传过来, 就取
 */
@Data
@Validated
@ConfigurationProperties(prefix = Customs179Properties.PREFIX)
public class Customs179Properties {
    public static final String PREFIX = "eport.signature.report.customs179";
    /**
     * 电商平台代码
     */
    @NotBlank(message = "电商平台代码不能为空；如果你想对接CEBMessage进出口单, 先随便填一个，后面改成正确的")
    private String ebpCode;
    /**
     * 数据上报服务器URL地址，链接格式: https://域名我不告诉你/ceb2grab/grab/realTimeDataUpload
     *
     * @apiNote 没有配置的话采用项目内置的URL地址
     */
    private String server;
    /* 暂先注释以下参数 */
    /**
     * 收款账号
     */
    //private String recpAccount;
    /**
     * 收款企业代码
     */
    //private String recpCode;
    /**
     * 收款企业名称
     */
    //private String recpName;
}
