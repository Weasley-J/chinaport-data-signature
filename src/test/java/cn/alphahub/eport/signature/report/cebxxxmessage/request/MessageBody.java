package cn.alphahub.eport.signature.report.cebxxxmessage.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageBody {
    //加密后属性
    private String data;
}
