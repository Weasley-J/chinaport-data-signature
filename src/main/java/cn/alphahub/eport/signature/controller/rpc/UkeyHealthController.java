package cn.alphahub.eport.signature.controller.rpc;

import cn.alphahub.eport.signature.basic.domain.Result;
import cn.alphahub.eport.signature.config.UkeyAccessClientProperties.Command;
import cn.alphahub.eport.signature.config.UkeyHealth;
import cn.alphahub.eport.signature.entity.ConsoleOutput;
import cn.alphahub.eport.signature.support.CommandClient;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
    private UkeyHealth ukeyHealth;

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
        ConsoleOutput consoleOutput = ukeyHealth.fixUkey(command);
        if (null == consoleOutput) {
            return Result.error("找不到重启ukey的可执行文件：SetAccessControl.exe, 请检查你的配置是否正确: --eport.signature.ukey.health.endpoint.client-name");
        }
        return Result.ok(consoleOutput);
    }

    /**
     * 会在将终端输出同步写给浏览器
     */
    @GetMapping("/endpoint/exec")
    public void exec(@RequestParam("cmd") String cmd, HttpServletResponse response) throws IOException {
        log.info("执行指令: {}", cmd);
        response.addHeader("Content-Type", "text/event-stream; charset=utf-8");
        CommandClient commandClient = CommandClient.getSharedInstance();
        commandClient.execute(cmd, response.getOutputStream());
    }

}
