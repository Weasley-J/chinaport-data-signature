package cn.alphahub.eport.signature.controller.rpc;

import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.config.UkeyAccessClientProperties.Command;
import cn.alphahub.eport.signature.config.UkeyHealthHelper;
import cn.alphahub.eport.signature.entity.ConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ukey健康Controller
 *
 * @author lwj
 * @version 1.0.9
 */
@Slf4j
@RestController
@RequestMapping("/rpc/ukey/health")
public class UkeyHealthController {

    @Autowired
    private UkeyHealthHelper ukeyHealthHelper;

    /**
     * ukey健康指令
     *
     * @return 控制太输出
     * @apiNote <ul>
     * <b>支持command类型</b>
     * <li>RESTART: 重启控件(较多使用)</li>
     * <li>START: 启动控件</li>
     * <li>STOP: 停止控件</li>
     * <li>REPAIR: 修复证书</li>
     * </ul>
     * @see Command
     */
    @PostMapping("/endpoint/{command}")
    public Result<ConsoleOutput> endpoint(@PathVariable("command") Command command) {
        log.info("执行ukey健康指令: {}", command.getDesc());
        ConsoleOutput consoleOutput = ukeyHealthHelper.fixUkey(command);
        if (null == consoleOutput) {
            return Result.error("找不到重启ukey的可执行文件：SetAccessControl.exe, 请检查你的配置是否正确: --eport.signature.ukey.health.endpoint.client-name");
        }
        return Result.ok(consoleOutput);
    }

}
