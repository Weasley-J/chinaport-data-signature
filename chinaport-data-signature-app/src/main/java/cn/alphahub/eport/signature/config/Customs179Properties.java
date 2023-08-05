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
     *
     * @apiNote 电商平台海关注册登记编号或统一社会信用代码。境内企业需据实填写海关注册登记编号或统一社会信用代码，境外平台企业如无法提供电商平台代码可填“无”。
     */
    @NotBlank(message = "电商平台代码不能为空；如果你想对接CEBMessage进出口单, 先随便填一个，后面改成正确的")
    private String ebpCode;
    /**
     * 数据上报服务器URL地址，链接格式: https://域名联系海关获取/ceb2grab/grab/realTimeDataUpload
     *
     * @apiNote 没有配置的话采用项目内置的URL地址
     */
    private String server;
}
