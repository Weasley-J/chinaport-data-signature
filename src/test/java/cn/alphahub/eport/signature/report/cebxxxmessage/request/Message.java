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
public class Message implements Serializable {
    private MessageHead MessageHead;
    private MessageBody MessageBody;
}
