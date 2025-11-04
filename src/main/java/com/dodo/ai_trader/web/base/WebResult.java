package com.dodo.ai_trader.web.base;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Author: shiwen
 * Date: 2025/3/3 14:45
 * Description:
 */
@Data
@ToString
public class WebResult<T> implements Serializable {

    private static final long serialVersionUID = 4751565867507258829L;

    private Integer code;

    private T data;

    private Boolean canRetry = false;

    private String errorCode;

    private String msg;

    public WebResult(int code, String msg, T data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public WebResult(int code, String errorCode, String msg, T data) {
        this.code = code;
        this.data = data;
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public WebResult(int code, String errorCode, String msg, T data, Boolean canRetry) {
        this.code = code;
        this.canRetry = canRetry;
        this.data = data;
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public static <T> WebResult<T> success(T data) {
        return new WebResult<>(0, "0000", data);
    }

    public static <T> WebResult<T> unauthorized() {
        return new WebResult<>(401, "401", null);
    }

    public static <T> WebResult<T> error(String errorCode, String msg) {

        return new WebResult<>(500, errorCode, msg, null);
    }

    public static <T> WebResult<T> error(String errorCode, String msg, Boolean canRetry) {

        return new WebResult<>(500, errorCode, msg, null, canRetry);
    }
}
