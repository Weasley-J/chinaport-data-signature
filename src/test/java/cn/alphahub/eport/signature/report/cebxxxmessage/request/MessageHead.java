package cn.alphahub.eport.signature.report.cebxxxmessage.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageHead implements Serializable {
    private String MessageId = "";
    private String MessageType;
    private String SenderID = "SHOP";
    private String ReceiverID = "PORT";
    private String SendTime;
    private String Version = "1.0";
}
