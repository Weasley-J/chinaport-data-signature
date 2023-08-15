package cn.alphahub.eport.signature.base.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 自定义异常
 *
 * @author liuwenjing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SignException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 异常消息
     */
    private String msg;

    /**
     * 错误码, 默认500
     */
    private int code = 500;

    public SignException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public SignException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public SignException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}
