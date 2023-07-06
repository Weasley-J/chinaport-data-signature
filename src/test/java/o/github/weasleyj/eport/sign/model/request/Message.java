package o.github.weasleyj.eport.sign.model.request;

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
public class Message implements Serializable {
    private MessageHead MessageHead;
    private MessageBody MessageBody;
}
