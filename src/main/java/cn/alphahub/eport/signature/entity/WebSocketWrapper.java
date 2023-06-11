package cn.alphahub.eport.signature.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * WebSocket上下文包装类
 *
 * @author weasley
 * @version 1.0
 * @date 2022/2/13
 */
@Data
@Accessors(chain = true)
public class WebSocketWrapper implements Serializable {
    /**
     * websocket发送的数据载荷
     */
    private String payload;
    /**
     * 当前线程
     */
    private AtomicReference<Thread> threadReference;
    /**
     * CEBXxxMessage加签请求入参
     */
    private SignRequest request;
    /**
     * 加签结果
     */
    private SignResult signResult;
}
