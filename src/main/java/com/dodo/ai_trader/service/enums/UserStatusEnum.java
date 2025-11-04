package com.dodo.ai_trader.service.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Author: shiwen
 * Date: 2025/5/17 14:40
 * Description:
 */
public enum UserStatusEnum {

    DISABLE("DISABLE", "不可用"),
    ENABLE("ENABLE", "可用"),
    FREEZE("FREEZE", "冻结"),
    PRE("PRE", "预注册"),
    ;

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    UserStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserStatusEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (UserStatusEnum userStatusEnum : UserStatusEnum.values()) {
            if (userStatusEnum.getCode().equals(code)) {
                return userStatusEnum;
            }
        }
        return null;
    }
}
