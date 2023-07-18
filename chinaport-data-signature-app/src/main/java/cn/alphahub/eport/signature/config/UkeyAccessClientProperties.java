package cn.alphahub.eport.signature.config;

import cn.alphahub.eport.signature.support.CommandClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static cn.alphahub.eport.signature.config.ICommand.EPORT_ACCESS_CONTROL;
import static cn.alphahub.eport.signature.config.ICommand.EPORT_WS_SERVER;
import static cn.alphahub.eport.signature.config.UkeyProperties.PREFIX;

/**
 * An interface to build command
 */
@FunctionalInterface
interface ICommand extends Serializable {
    /**
     * WebSocketServer.exe
     */
    String EPORT_WS_SERVER = "CnEport.Pub.WebSocketServer.exe";
    /**
     * SetAccessControl.exe
     */
    String EPORT_ACCESS_CONTROL = "SetAccessControl.exe";

    /**
     * To build command
     *
     * @param exeClient e.g: C:\中国电子口岸客户端控件\SetAccessControl.exe
     * @return command script
     */
    String build(String exeClient);
}

/**
 * Ukey自我重启相关
 * <p>
 * Windows上的可执行文件: C:\中国电子口岸客户端控件\SetAccessControl.exe
 *
 * @author weasley
 * @version 1.0.0
 * @apiNote 可执行文件 SetAccessControl.exe 的安装位置以实际为准
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = PREFIX + ".health.endpoint")
public class UkeyAccessClientProperties {

    /**
     * Windows上重启的ukey可执行文件全限定文件名称
     *
     * @apiNote 建议指定 clientName，可以避免全盘查找 SetAccessControl.exe, 提高性能
     * @apiNote 全限定文件名称，包含文件后缀, 建议不要安装到中文目录里面，如: C:\中国电子口岸客户端控件\SetAccessControl.exe
     */
    private String clientName;

    /**
     * 查找中国电子口岸客户端控件重启客户端的Windows可执行文件的具体位置
     *
     * @return SetAccessControl.exe 文件的绝对路径
     * @apiNote 以下命令行只适用于Windows平台，其他平台报错直接忽略
     */
    public String findClient() {
        if (!SystemUtils.IS_OS_WINDOWS) return null;
        CommandClient sharedInstance = CommandClient.getSharedInstance();

        List<String> accessControlStdOutList;
        List<String> eportWsServerStdOutList;

        sharedInstance.execute("where " + EPORT_ACCESS_CONTROL);
        accessControlStdOutList = new ArrayList<>(sharedInstance.getStdOutList());
        if (CollectionUtils.isEmpty(accessControlStdOutList)) return null;

        sharedInstance.execute("where " + EPORT_WS_SERVER);
        eportWsServerStdOutList = new ArrayList<>(sharedInstance.getStdOutList());
        if (CollectionUtils.isEmpty(eportWsServerStdOutList)) return null;

        String accessControlStdOut = FilenameUtils.getFullPathNoEndSeparator(accessControlStdOutList.get(0));
        String eportWsServerStdOut = FilenameUtils.getFullPathNoEndSeparator(eportWsServerStdOutList.get(0));
        if (accessControlStdOut.equals(eportWsServerStdOut)) {
            log.info("找到u-key Windows客户端执行文件： {} {}", EPORT_WS_SERVER, EPORT_ACCESS_CONTROL);
            return accessControlStdOutList.get(0);
        }
        return null;
    }

    /**
     * 执行指令
     * <pre>
     * -h 或 -help: 帮助
     * -restart: 重启控件
     * -start: 启动控件
     * -stop: 停止控件
     * -repair: 修复证书
     * </pre>
     */
    @Getter
    @AllArgsConstructor
    public enum Command implements ICommand {
        /**
         * 重启控件
         */
        RESTART("-restart", "重启控件"),
        /**
         * 启动控件
         */
        START("-start", "启动控件"),
        /**
         * 停止控件
         */
        STOP("-stop", "停止控件"),
        /**
         * 修复证书
         */
        REPAIR("-repair", "修复证书"),
        /**
         * 缺省符，无业务意义
         */
        DEFAULT("", "缺省符");

        /**
         * 指令
         */
        private final String cmd;
        /**
         * 描述
         */
        private final String desc;

        public String getCmd() {
            if (RESTART.cmd.equals(cmd)) {
                return cmd + " -i";
            }
            return cmd;
        }

        @Override
        public String build(String exeClient) {
            if (null == exeClient) return Strings.EMPTY;
            if (StringUtils.isBlank(this.getCmd())) {
                return "cmd /c \"" + exeClient + "\"";
            }
            return "cmd /c \"" + exeClient + " " + this.getCmd() + "\"";
        }
    }

}
