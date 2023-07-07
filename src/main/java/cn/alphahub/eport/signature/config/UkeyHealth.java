package cn.alphahub.eport.signature.config;

import cn.alphahub.eport.signature.config.UkeyAccessClientProperties.Command;
import cn.alphahub.eport.signature.entity.ConsoleOutput;
import cn.alphahub.eport.signature.support.CommandClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ukey Self Recovery
 *
 * @author weasley
 * @version 1.0.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({UkeyAccessClientProperties.class})
public class UkeyHealth {
    private final UkeyAccessClientProperties ukeyAccessClientProperties;

    public UkeyHealth(UkeyAccessClientProperties ukeyAccessClientProperties) {
        this.ukeyAccessClientProperties = ukeyAccessClientProperties;
    }

    /**
     * 项目启动回调
     *
     * @return 先回调一遍，如果没有配置 clientName 提前缓存填充Windows上的ukey重启的可执行exe
     * @apiNote <a href="https://app.singlewindow.cn/cas/login?service=http%3A%2F%2Fwww.singlewindow.cn%2Fsinglewindow%2Flogin.jspx">
     * <img src="https://weasley.oss-cn-shanghai.aliyuncs.com/Photos/image-20230707193959470.png">电子口岸C卡/Key客户端</img></a>
     * @see <a href="https://update.singlewindow.cn/downloads/EportClientSetup_V1.5.46.exe">电子口岸C卡/Key客户端控件：EportClientSetup_V1.5.46.exe</a>
     */
    @Bean
    ApplicationRunner ukeyPreCallbackRunner() {
        if (SystemUtils.IS_OS_WINDOWS && StringUtils.isNotBlank(ukeyAccessClientProperties.getClientName())
                && (!FileUtils.getFile(ukeyAccessClientProperties.getClientName()).exists())) {
            log.error("可执行文件 {} 不存在，请检查你的配置; \n如果你没有安装【中国电子口岸C卡/Key客户端控件】请前往官网下载(卡介质登录界面)下载: {}; \n或者点击链接下载：{}",
                    ukeyAccessClientProperties.getClientName(),
                    "https://app.singlewindow.cn/cas/login?service=http%3A%2F%2Fwww.singlewindow.cn%2Fsinglewindow%2Flogin.jspx",
                    "https://update.singlewindow.cn/downloads/EportClientSetup_V1.5.46.exe");
            return args -> {
            };
        }
        return args -> fixUkey(Command.DEFAULT);
    }

    /**
     * 执行Ukey健康相关指令
     *
     * @param command 执行指令
     * @return 命令行
     */
    public ConsoleOutput fixUkey(Command command) {
        if (!SystemUtils.IS_OS_WINDOWS) {
            log.warn("仅支持在Windows平台执行Ukey健康相关指令, 如: {}", Command.RESTART.getCmd());
            return null;
        }
        if (StringUtils.isBlank(ukeyAccessClientProperties.getClientName())) {
            ukeyAccessClientProperties.setClientName(ukeyAccessClientProperties.findClient());
        }
        CommandClient commandClient = CommandClient.getSharedInstance();
        commandClient.execute(command.build(ukeyAccessClientProperties.getClientName()));
        return new ConsoleOutput(commandClient.getStdOutList(), commandClient.getErrorOutList());
    }

}
