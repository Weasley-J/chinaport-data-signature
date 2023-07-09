package cn.alphahub.eport.signature.base.advice;

import cn.alphahub.eport.signature.base.domain.Result;
import cn.alphahub.eport.signature.base.enums.BizCodeEnum;
import cn.alphahub.eport.signature.base.exception.SignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 全局异常处理（集中处理全局异常处理）
 *
 * @author liuwenjing
 */
@Slf4j
@RestControllerAdvice(basePackages = {"cn.alphahub"})
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     *
     * @param e 业务异常
     * @return 错误信息
     */
    @ExceptionHandler(SignException.class)
    public Result<SignException> signExceptionHandler(SignException e) {
        return Result.fail(e.getCode(), StringUtils.hasText(e.getMsg()) ? e.getMsg() : e.getLocalizedMessage());
    }

    /**
     * 找不到处理程序异常
     *
     * @param e 异常
     * @return 错误信息
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<Object> notFoundExceptionHandler(NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return Result.fail(404, "路径不存在，请检查路径是否正确: " + e.getMessage());
    }

    /**
     * 重复键异常
     *
     * @param e 异常
     * @return 错误信息
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<Object> duplicateKeyExceptionHandler(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return Result.fail("数据库中已存在该记录: " + e.getMessage());
    }

    /**
     * 数据绑定异常
     *
     * @param e 异常
     * @return 错误信息
     */
    @ExceptionHandler(BindException.class)
    public Result<Object> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        Map<String, Object> errorMap = new LinkedHashMap<>();
        for (ObjectError error : e.getAllErrors()) {
            errorMap.putIfAbsent(error.getObjectName(), error.getDefaultMessage());
        }
        return Result.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMessage(), errorMap);
    }

    /**
     * 请求方式不支持
     *
     * @param e       http request method not supported exception
     * @param request http servlet request
     * @return 错误信息
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", uri, e.getMethod());
        return Result.error("请求地址'" + uri + "',不支持'" + e.getMethod() + "'请求");
    }

    /**
     * 超出最大上传大小异常
     *
     * @param e 超出最大上传大小异常
     * @return 错误信息
     */
    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    public Result<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("超出最大上传大小异常: {}", e.getMessage(), e);
        return Result.error("上传文件超过允许的最大上传大小:" + e.getLocalizedMessage(), "最大文件大小：" + e.getMaxUploadSize() + "(bytes)");
    }

    /**
     * 数据校验异常
     *
     * @param e 异常对象
     * @return 数据校验异常具体错误信息
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Object> handleValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        Map<String, Map<String, Object>> errorMap = new LinkedHashMap<>();
        Map<String, Object> globalErrorMap = new LinkedHashMap<>();
        Map<String, Object> fieldErrorMap = new LinkedHashMap<>();
        result.getGlobalErrors().forEach(objectError -> {
            globalErrorMap.putIfAbsent(objectError.getObjectName(), e.getTarget());
            globalErrorMap.putIfAbsent("errorMsg", objectError.getDefaultMessage());
        });
        result.getFieldErrors().forEach(fieldError -> fieldErrorMap.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage()));
        errorMap.putIfAbsent("globalError", globalErrorMap);
        errorMap.putIfAbsent("fieldError", fieldErrorMap);
        log.error("数据校验异常：{}，异常类型：{}", e.getMessage(), e.getClass());
        return Result.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMessage(), errorMap);
    }

    /**
     * Exception异常
     *
     * @param e 异常
     * @return 错误信息
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> exceptionHandler(Exception e) {
        log.error("异常信息：{}", e.getLocalizedMessage(), e);
        return Result.fail(e.getMessage());
    }

    /**
     * RuntimeException异常
     *
     * @param re 异常
     * @return 错误提示
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Object> handleUnknownException(RuntimeException re) {
        log.error("运行期间抛出的异常：{}", re.getMessage(), re);
        return Result.error(re.getMessage());
    }

    /**
     * Throwable系统未知异常
     *
     * @param throwable 异常对象
     * @return 异常提示
     */
    @ExceptionHandler(value = Throwable.class)
    public Result<Object> handleException(Throwable throwable) {
        log.error("错误信息: ", throwable);
        return Result.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), BizCodeEnum.UNKNOWN_EXCEPTION.getMessage(), "Caused by:" + throwable.getLocalizedMessage());
    }
}
