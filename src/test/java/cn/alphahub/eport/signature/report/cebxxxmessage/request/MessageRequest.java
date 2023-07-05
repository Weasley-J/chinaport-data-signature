package cn.alphahub.eport.signature.report.cebxxxmessage.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Enter the description of this class here
 *
 * @author weasley
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest  implements Serializable {
    private Message Message;
}
