package com.dodo.ai_trader.service.utils;

import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import com.dodo.ai_trader.service.exception.BizException;
import org.apache.commons.lang3.StringUtils;

/**
 * Author: shiwen
 * Date: 2024/9/4 10:58
 * Description:
 */
public class AssertUtil {
    public static void isTrue(boolean expression, ErrorCodeEnum errorCodeEnum, boolean canRetry) {
        if (!expression) {
            throw new BizException(errorCodeEnum.getCode(), errorCodeEnum.getMsg(), canRetry);
        }
    }

    public static void isTrue(boolean expression, ErrorCodeEnum errorCodeEnum, String message, boolean canRetry) {
        if (!expression) {
            throw new BizException(errorCodeEnum.getCode(), message, canRetry);
        }
    }

    public static void notNull(Object object, ErrorCodeEnum errorCodeEnum, boolean canRetry) {
        if (object == null) {
            throw new BizException(errorCodeEnum.getCode(), errorCodeEnum.getMsg(), canRetry);
        }
    }

    public static void notNull(Object object, ErrorCodeEnum errorCodeEnum, String message, boolean canRetry) {
        if (object == null) {
            throw new BizException(errorCodeEnum.getCode(), message, canRetry);
        }
    }

    public static void notEmpty(String str, ErrorCodeEnum errorCodeEnum, boolean canRetry) {
        if (StringUtils.isBlank(str)) {
            throw new BizException(errorCodeEnum.getCode(), errorCodeEnum.getMsg(), canRetry);
        }
    }

    public static void notEmpty(String str, ErrorCodeEnum errorCodeEnum, String message, boolean canRetry) {
        if (StringUtils.isBlank(str)) {
            throw new BizException(errorCodeEnum.getCode(), message, canRetry);
        }
    }
}
