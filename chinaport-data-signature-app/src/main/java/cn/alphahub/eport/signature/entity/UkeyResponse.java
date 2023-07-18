package cn.alphahub.eport.signature.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * u-key加签返回结果包装类
 * <p>以下对象模型禁止修改</p>
 *
 * @author Weasley
 * @date 2022-02-12 19:7:6
 */
public class UkeyResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -1L;
    /**
     * 会话id，用来区分是哪次加签
     */
    private Integer _id;
    private String _method;
    private String _status;
    private Args _args;

    public UkeyResponse() {
    }

    public UkeyResponse(Integer _id, String _method, String _status, Args _args) {
        this._id = _id;
        this._method = _method;
        this._status = _status;
        this._args = _args;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String get_method() {
        return _method;
    }

    public void set_method(String _method) {
        this._method = _method;
    }

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public Args get_args() {
        return _args;
    }

    public void set_args(Args _args) {
        this._args = _args;
    }

    @Override
    public String toString() {
        return "UkeyResponse{" + "_id=" + _id + ", _method='" + _method + '\'' + ", _status='" + _status + '\'' + ", _args=" + _args + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UkeyResponse that)) return false;
        return Objects.equals(get_id(), that.get_id()) && Objects.equals(get_method(), that.get_method()) && Objects.equals(get_status(), that.get_status()) && Objects.equals(get_args(), that.get_args());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get_id(), get_method(), get_status(), get_args());
    }

    /**
     * u-key加签返回内层对象，我们要的数据在这里面
     */
    public static class Args implements Serializable {
        @Serial
        private static final long serialVersionUID = -1L;

        private Boolean Result;
        /**
         * 我们要的数据在这里面
         */
        private List<String> Data;
        private List<String> Error;

        public Args() {
        }

        public Args(Boolean result, List<String> data, List<String> error) {
            Result = result;
            Data = data;
            Error = error;
        }

        public Boolean getResult() {
            return Result;
        }

        public void setResult(Boolean result) {
            Result = result;
        }

        public List<String> getData() {
            return Data;
        }

        public void setData(List<String> data) {
            Data = data;
        }

        public List<String> getError() {
            return Error;
        }

        public void setError(List<String> error) {
            Error = error;
        }

        @Override
        public String toString() {
            return "Args{" + "Result=" + Result + ", Data=" + Data + ", Error=" + Error + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Args args)) return false;
            return Objects.equals(getResult(), args.getResult()) && Objects.equals(getData(), args.getData()) && Objects.equals(getError(), args.getError());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getResult(), getData(), getError());
        }
    }
}