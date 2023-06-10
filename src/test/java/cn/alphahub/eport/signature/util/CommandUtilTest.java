package cn.alphahub.eport.signature.util;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

class CommandUtilTest {

    @Test
    void execute() {
        CommandUtil.getSharedInstance().execute("""
                ping baidu.com
                """);


    }

    @Test
    void execute02() {
        CommandUtil.getSharedInstance().execute("""
                java -version
                """);
    }

    @Test
    void execute2() {
        CommandUtil.getSharedInstance().execute("""
                java -jar /Users/weasley/Development/IdeaProjects/chinaport-data-signature/target/chinaport-data-signature.jar
                """);
    }

    @Test
    void execute3() {
        CommandUtil commandUtil = CommandUtil.getSharedInstance();
        commandUtil.execute("brew update", "brew upgrade");
        commandUtil.getStdOutList();
    }

    @Test
    void execute4() {
        CommandUtil commandUtil = CommandUtil.getSharedInstance();
        commandUtil.execute("curl -X GET -i http://localhost:8080/rpc/eport/signature/test/ceb");
        System.out.println(JSONUtil.toJsonStr(commandUtil));
        for (int i = 0; i < 3; i++) {
            CompletableFuture.runAsync(() -> {

            });
        }
    }

    @Test
    void execute5() {
        CommandUtil commandUtil = CommandUtil.getSharedInstance();
        for (int i = 1; i <= 3; i++) {
            commandUtil.execute("curl -X GET -i http://localhost:8080/rpc/eport/signature/test/ceb");
            System.out.println("第 " + i + " 次结束：\n" + JSONUtil.toJsonStr(commandUtil));
        }
    }

    @Test
    void execute6() {
        CommandUtil commandUtil = CommandUtil.getSharedInstance();
        for (int i = 1; i <= 3; i++) {
            commandUtil.execute("curl -X GET -i http://www.baidu.com");
            System.out.println("第 " + i + " 次结束：\n" + JSONUtil.toJsonPrettyStr(commandUtil));
        }
    }

}
