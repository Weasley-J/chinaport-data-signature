package o.github.weasleyj.china.eport.sign.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MessageHead implements Serializable {
    private String MessageId = "";
    private String MessageType;
    private String SenderID = "SHOP";
    private String ReceiverID = "PORT";
    private String SendTime;
    private String Version = "1.0";
}
