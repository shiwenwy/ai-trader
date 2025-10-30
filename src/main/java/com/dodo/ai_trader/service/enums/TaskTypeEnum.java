package com.dodo.ai_trader.service.enums;

import io.micrometer.common.util.StringUtils;

/**
 * Author: shiwen
 * Date: 2025/4/14 10:56
 * Description:
 */
public enum TaskTypeEnum {

    TOKEN_INFO_SYNC("TOKEN_INFO_SYNC", "代币信息同步"),
    TOKEN_DELIST("TOKEN_DELIST", "代币下架"),
    ;

    private String code;

    private String desc;

    TaskTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static TaskTypeEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (TaskTypeEnum taskTypeEnum : TaskTypeEnum.values()) {
            if (taskTypeEnum.getCode().equals(code)) {
                return taskTypeEnum;
            }
        }
        return null;
    }
}
