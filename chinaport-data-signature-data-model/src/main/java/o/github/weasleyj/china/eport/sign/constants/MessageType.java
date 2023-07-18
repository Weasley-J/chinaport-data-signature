package o.github.weasleyj.china.eport.sign.constants;

import o.github.weasleyj.china.eport.sign.IMessageType;

/**
 * Message Type
 *
 * @author weasley
 * @version 1.0.0
 */
public enum MessageType implements IMessageType {
    CEB311Message("CEB311Message"),
    CEB621Message("CEB621Message"),
    ;
    private final String messageType;

    MessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String getMessageType() {
        return messageType;
    }
}
