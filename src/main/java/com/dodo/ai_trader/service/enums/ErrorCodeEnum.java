package com.dodo.ai_trader.service.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Author: shiwen
 * Date: 2024/9/4 11:04
 * Description:
 */
public enum ErrorCodeEnum {

    SYSTEM_ERROR("9999", "系统异常"),
    LOCK_ERROR("9998", "锁异常"),
    DATABASE_ERROR("9997", "数据库异常"),
    MQ_SEND_ERROR("9996", "消息队列发送失败"),
    CACHE_ERROR("9995", "缓存异常"),

    USER_NOT_EXIST("1000", "用户不存在"),
    PARAM_ERROR("1001", "参数异常"),
    DATA_CONFLICT("1002", "数据不一致"),
    ACCOUNT_NOT_EXIST("1003", "账户不存在"),
    ACCOUNT_NOT_ENOUGH("1004", "余额不足"),
    RECON_NOT_FINISHED("1005", "对账未完成"),
    WALLET_NOT_MATCH("1006", "钱包不匹配"),
    WALLET_NOT_AVAILABLE("1007", "无可用钱包"),
    ORDER_PROCESSING("1008", "订单处理中"),
    MERCHANT_NOT_EXIST("1009", "商户不存在"),
    GAS_NOT_ENOUGH("1010", "gas不足"),
    USER_PASSWORD_ERROR("1011", "用户密码错误"),
    USER_EXIST("1012", "用户已存在"),
    USER_NOT_LOGIN("1013", "用户未登录或登录已过期"),
    NOTIFY_FAILED("1014", "通知失败"),
    ;

    private String code;
    private String msg;

    ErrorCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ErrorCodeEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (ErrorCodeEnum item : ErrorCodeEnum.values()) {
            if (StringUtils.equals(item.getCode(), code)) {
                return item;

            }
        }
        return null;
    }
}
