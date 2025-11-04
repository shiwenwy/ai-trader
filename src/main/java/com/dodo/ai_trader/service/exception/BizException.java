package com.dodo.ai_trader.service.exception;


import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import lombok.Data;

/**
 * Author: shiwen
 * Date: 2024/9/4 10:59
 * Description:
 */
@Data
public class BizException extends RuntimeException{

    private static final long serialVersionUID = 9158316345374690448L;

    private String code;

    private String msg;

    private boolean canRetry;

    public BizException(ErrorCodeEnum errorCodeEnum, boolean canRetry) {
        this(errorCodeEnum.getCode(), errorCodeEnum.getMsg(), canRetry);
    }

    public BizException(String code, String msg, boolean canRetry) {
        this.code = code;
        this.msg = msg;
        this.canRetry = canRetry;
    }

    public BizException(String message, String code, String msg, boolean canRetry) {
        super(message);
        this.code = code;
        this.msg = msg;
        this.canRetry = canRetry;
    }

    public BizException(String message, Throwable cause, String code, String msg, boolean canRetry) {
        super(message, cause);
        this.code = code;
        this.msg = msg;
        this.canRetry = canRetry;
    }

    public BizException(Throwable cause, String code, String msg, boolean canRetry) {
        super(cause);
        this.code = code;
        this.msg = msg;
        this.canRetry = canRetry;
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, String msg, boolean canRetry) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.msg = msg;
        this.canRetry = canRetry;
    }
}
