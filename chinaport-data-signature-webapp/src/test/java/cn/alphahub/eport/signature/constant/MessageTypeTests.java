package cn.alphahub.eport.signature.constant;

import io.github.weasleyj.china.eport.sign.constants.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * Message Type Tests
 *
 * @author weasley
 * @version 1.0.0
 */
@Slf4j
class MessageTypeTests {
    @Test
    void contextLoads() {
        MessageType[] values = MessageType.values();
        log.info("电子口岸XML报文类型数量：{}", values.length);
        for (MessageType messageType : values) {
            System.out.println(messageType.getDesc() + " " + messageType.getType());
        }
    }
}
