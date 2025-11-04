package com.dodo.ai_trader.web.interceptor;


import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import com.dodo.ai_trader.service.exception.BizException;
import com.dodo.ai_trader.service.utils.LogUtil;
import com.dodo.ai_trader.web.base.WebResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常拦截
 * Author: shiwen
 * Date: 2025/3/5 10:33
 * Description:
 */
@RestControllerAdvice
public class SystemExceptionHandler {

    @ExceptionHandler(BizException.class)
    public Object handleBizException(BizException bizException) {
        LogUtil.error("SystemExceptionHandler.BizException", bizException);
        return WebResult.error(bizException.getCode(), bizException.getMsg());
    }

    @ExceptionHandler(Throwable.class)
    public Object handleThrowable(Exception ex) {
        LogUtil.error("SystemExceptionHandler.handleThrowable", ex);
        return WebResult.error(ErrorCodeEnum.SYSTEM_ERROR.getCode(), ex.getMessage());
    }
}
