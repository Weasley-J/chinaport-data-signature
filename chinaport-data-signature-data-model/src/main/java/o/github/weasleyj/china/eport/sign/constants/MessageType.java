package o.github.weasleyj.china.eport.sign.constants;

import o.github.weasleyj.china.eport.sign.IMessageType;

/**
 * Message Type
 *
 * @author weasley
 * @version 1.0.0
 */
public enum MessageType implements IMessageType {
    /* 进口单 */
    CEB311Message("CEB311Message"),
    CEB621Message("CEB621Message"),
    /* 出口单 */
    CEB303Message("CEB303Message"),
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
