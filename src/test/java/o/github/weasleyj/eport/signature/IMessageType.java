package o.github.weasleyj.eport.signature;

import java.io.Serializable;

/**
 * The interface for get message type: CEB311Message|CEB621Message|...
 */
@FunctionalInterface
public interface IMessageType extends Serializable {
    /**
     * @return 消息类型：CEB311Message|CEB621Message|...
     */
    String getMessageType();
}
