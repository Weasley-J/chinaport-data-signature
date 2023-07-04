package cn.alphahub.eport.signature.report.cebxxxmessage.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Message Type
 *
 * @author weasley
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
public enum MessageType {
    CEB311Message("CEB311Message"),
    CEB621Message("CEB621Message"),
    ;
    private final String type;
}
