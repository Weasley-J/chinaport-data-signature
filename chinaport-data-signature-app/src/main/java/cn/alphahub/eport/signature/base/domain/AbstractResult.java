package cn.alphahub.eport.signature.base.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据返回封装顶层抽象类
 * <p>
 * 顶层抽象类, 可以使用我已经写好的BaseResult<T>, 也可以选择自己实现AbstractResult<T>, {@code permits}关键字加上类名即可
 * </p>
 *
 * @author liuwenjing
 * @version 1.2.0
 * @date 2022年2月11日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract sealed class AbstractResult<T> implements Serializable permits Result {
    /**
     * 返回消息
     */
    private String message;

    /**
     * 是否成功
     */
    private Boolean success = false;

    /**
     * 响应时间戳
     */
    private String timestamp;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 响应数据
     */
    private T data;
}
