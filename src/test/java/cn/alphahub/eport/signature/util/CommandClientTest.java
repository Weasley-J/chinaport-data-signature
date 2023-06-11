package cn.alphahub.eport.signature.util;

import cn.alphahub.eport.signature.support.CommandClient;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

class CommandClientTest {

    @Test
    void execute() {
        CommandClient.getSharedInstance().execute("""
                ping baidu.com
                """);


    }

    @Test
    void execute02() {
        CommandClient.getSharedInstance().execute("""
                java -version
                """);
    }

    @Test
    void execute2() {
        CommandClient.getSharedInstance().execute("""
                java -jar /Users/weasley/Development/IdeaProjects/chinaport-data-signature/target/chinaport-data-signature.jar
                """);
    }

    @Test
    void execute3() {
        CommandClient commandClient = CommandClient.getSharedInstance();
        commandClient.execute("brew update", "brew upgrade");
        commandClient.getStdOutList();
    }

    @Test
    void execute4() {
        CommandClient commandClient = CommandClient.getSharedInstance();
        commandClient.execute("curl -X GET -i http://localhost:8080/rpc/eport/signature/test/ceb");
        System.out.println(JSONUtil.toJsonStr(commandClient));
        for (int i = 0; i < 3; i++) {
            CompletableFuture.runAsync(() -> {

            });
        }
    }

    @Test
    void execute5() {
        CommandClient commandClient = CommandClient.getSharedInstance();
        for (int i = 1; i <= 3; i++) {
            commandClient.execute("curl -X GET -i http://localhost:8080/rpc/eport/signature/test/ceb");
            System.out.println("第 " + i + " 次结束：\n" + JSONUtil.toJsonStr(commandClient));
        }
    }

    @Test
    void execute6() {
        CommandClient commandClient = CommandClient.getSharedInstance();
        for (int i = 1; i <= 3; i++) {
            commandClient.execute("curl -X GET -i http://www.baidu.com");
            System.out.println("第 " + i + " 次结束：\n" + JSONUtil.toJsonPrettyStr(commandClient));
        }
    }

}
