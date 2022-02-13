package cn.alphahub.eport.signature.basic.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 数据返回封装
 * <p>通用面向对象基础返回数据封装</p>
 *
 * @param <T> 返回数据对象
 * @author liuwenjing
 * @version 1.2.0
 * @date 2022年2月11日
 * @see AbstractResult
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class Result<T> extends AbstractResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -7804054241710086L;

    /**
     * 初始化一个新创建的 Result 对象，使其表示一个空消息
     */
    public Result() {
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param code    状态码
     * @param msg     返回内容
     * @param success 成功状态
     */
    public Result(Integer code, String msg, Boolean success) {
        this.setCode(code);
        this.setMessage(msg);
        this.setSuccess(success);
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param success 成功状态
     */
    public Result(Boolean success) {
        this.setSuccess(success);
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param success 成功状态
     * @param message 返回消息
     */
    public Result(Boolean success, String message) {
        this.setSuccess(success);
        this.setMessage(message);
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param code    状态码
     * @param message 返回消息
     */
    public Result(Integer code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param success 成功提示
     * @param message 返回消息
     * @param data    数据对象
     */
    public Result(Boolean success, String message, T data) {
        this.setSuccess(success);
        this.setMessage(message);
        this.setData(data);
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public Result(Integer code, String msg, T data) {
        this.setCode(code);
        this.setMessage(msg);
        this.setData(data);
    }

    /**
     * 类加载时初始化一个BaseResult<T>对象
     *
     * @param code    状态码
     * @param msg     返回消息
     * @param success 成功标志
     * @param <T>     数据对象
     * @return BaseResult封装好的数据对象
     */
    private static <T> Result<T> preCreate(Integer code, String msg, Boolean success, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(msg);
        result.setSuccess(success);
        result.setTimestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        result.setData(data);
        return result;
    }

    /**
     * 返回成功消息
     *
     * @param <T> 数据对象
     * @return 成功消息
     */
    public static <T> Result<T> ok() {
        return ok(200, "操作成功");
    }

    /**
     * 携带数据返回成功消息
     *
     * @param <T>  数据对象
     * @param data 返回消息
     * @return 数据对象
     */
    public static <T> Result<T> ok(T data) {
        return preCreate(200, "操作成功", true, data);
    }

    /**
     * 返回成功消息
     *
     * @param <T>  数据对象
     * @param code 状态码
     * @param msg  返回内容
     * @return 成功消息
     */
    public static <T> Result<T> ok(Integer code, String msg) {
        return preCreate(code, msg, true, null);
    }

    /**
     * 携带数据返回成功消息
     *
     * @param <T>  数据对象
     * @param msg  返回消息
     * @param data 数据载体
     * @return 封装的数据
     */
    public static <T> Result<T> ok(String msg, T data) {
        return preCreate(200, msg, true, data);
    }

    /**
     * 返回失败消息
     *
     * @param <T> 数据对象
     * @return 失败消息
     */
    public static <T> Result<T> fail() {
        return fail(500, "操作失败");
    }

    /**
     * 返回失败消息
     *
     * @param <T> 数据对象
     * @param msg 返回消息
     * @return 失败消息
     */
    public static <T> Result<T> fail(String msg) {
        return fail(500, msg);
    }

    /**
     * 返回错误消息
     *
     * @param <T>  数据对象
     * @param code 响应状态码
     * @param msg  响应消息
     * @return 错误消息
     */
    public static <T> Result<T> fail(Integer code, String msg) {
        return preCreate(code, msg, false, null);
    }

    /**
     * 返回错误消息
     *
     * @param <T>  数据对象
     * @param msg  响应消息
     * @param data 响应失败的消息体
     * @return 错误消息
     */
    public static <T> Result<T> fail(String msg, T data) {
        return preCreate(400, msg, false, data);
    }

    /**
     * 返回错误消息
     *
     * @param <T>  数据对象
     * @param code 响应状态码
     * @param msg  响应消息
     * @param data 响应失败的消息体
     * @return 错误消息
     */
    public static <T> Result<T> fail(Integer code, String msg, T data) {
        return preCreate(code, msg, false, data);
    }

    /**
     * 返回成功消息
     *
     * @param <T> 数据对象
     * @return 成功消息
     */
    public static <T> Result<T> success() {
        return Result.success(200, "操作成功");
    }

    /**
     * 携带数据返回成功消息
     *
     * @param <T>  数据对象
     * @param data 封装返回的数据对象
     * @return 成功消息
     */
    public static <T> Result<T> success(T data) {
        return Result.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param <T>  数据对象
     * @return 成功消息
     */
    public static <T> Result<T> success(Integer code, String msg) {
        return preCreate(code, msg, true, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @param <T>  数据对象
     * @return 成功消息
     */
    public static <T> Result<T> success(String msg, T data) {
        return preCreate(200, msg, true, data);
    }

    /**
     * 返回错误消息
     *
     * @param <T> 数据对象
     * @return 错误消息
     */
    public static <T> Result<T> error() {
        return Result.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @param <T> 数据对象
     * @return 警告消息
     */
    public static <T> Result<T> error(String msg) {
        return Result.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param <T>  数据对象
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> Result<T> error(String msg, T data) {
        return preCreate(500, msg, false, data);
    }

    /**
     * 返回错误消息
     *
     * @param <T>  数据对象
     * @param msg  返回内容
     * @param data 数据对象
     * @param code 状态码
     * @return 警告消息
     */
    public static <T> Result<T> error(Integer code, String msg, T data) {
        return preCreate(code, msg, false, data);
    }

    /**
     * 返回错误消息
     *
     * @param <T>  数据对象
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static <T> Result<T> error(Integer code, String msg) {
        return preCreate(code, msg, false, null);
    }

    /**
     * 封装数据并返回一个BaseResult实例
     *
     * @param data 数据
     * @return BaseResult实例
     */
    public Result<T> setResult(T data) {
        this.setData(data);
        return this;
    }
}

