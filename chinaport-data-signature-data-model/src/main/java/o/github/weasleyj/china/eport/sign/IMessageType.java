package o.github.weasleyj.china.eport.sign;

import java.io.Serializable;

/**
 * The interface for get message type: CEB311Message|CEB621Message|...
 *
 * @apiNote 获取各类进口单、出口单消息类型
 */
public interface IMessageType extends Serializable {
    /**
     * 消息类型：CEB311Message|CEB621Message|...
     */
    String getMessageType();
}
