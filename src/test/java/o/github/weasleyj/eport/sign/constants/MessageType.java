package o.github.weasleyj.eport.sign.constants;

import lombok.AllArgsConstructor;
import o.github.weasleyj.eport.sign.IMessageType;

/**
 * Message Type
 *
 * @author weasley
 * @version 1.0.0
 */
@AllArgsConstructor
public enum MessageType implements IMessageType {
    CEB311Message("CEB311Message"),
    CEB621Message("CEB621Message"),
    ;
    private final String type;

    @Override
    public String getMessageType() {
        return type;
    }
}
