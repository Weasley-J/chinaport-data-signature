package cn.alphahub.eport.signature.config;

import cn.alphahub.eport.signature.core.ChinaEportReportClient;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * 电子口岸XML报文传输的企业元数据配置
 *
 * @author weasley
 * @version 1.1.0
 */
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = ChinaEportProperties.PREFIX)
public class ChinaEportProperties {
    public static final String PREFIX = "eport.signature.report.ceb-message";
    /**
     * 传输企业代码，报文传输的企业代码(需要与接入客户端的企业身份一致)
     */
    @NotBlank(message = "配置文件中的报文传输的企业代码不能为空")
    private String copCode;
    /**
     * 传输企业名称，报文传输的企业名称
     */
    @NotBlank(message = "报文传输的企业名称不能为空")
    private String copName;
    /**
     * 报文传输编号，向中国电子口岸数据中心申请数据交换平台的用户编号
     */
    @NotBlank(message = "向中国电子口岸数据中心申请数据交换平台的用户编号不能为空")
    private String dxpId;
    /**
     * 海关服务器地址，缺省则采用Client中的密文作文默认Server URL
     *
     * @apiNote CEMMessage XML数据上报服务器地址，格式: http://ip:port
     * @see ChinaEportReportClient#EPORT_CEBMESSAGE_SERVER_ENCODE
     */
    private String server;
}
