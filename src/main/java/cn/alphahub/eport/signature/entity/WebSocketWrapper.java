package cn.alphahub.eport.signature.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;


/**
 * WebSocket包装类
 *
 * @author weasley
 * @version 1.0
 * @date 2022/2/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WebSocketWrapper implements Serializable {
    /**
     * websocket发送的数据载荷
     */
    private String payload;
    /**
     * 当前线程
     */
    private AtomicReference<Thread> threadAtomicReference = new AtomicReference<>();
    /**
     * CEBXxxMessage加签请求入参
     */
    private SignRequest request = new SignRequest();
    /**
     * 加签结果
     */
    private SignResult signResult = new SignResult();
}
