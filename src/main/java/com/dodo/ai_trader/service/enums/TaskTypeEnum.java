package com.dodo.ai_trader.service.enums;

import io.micrometer.common.util.StringUtils;

/**
 * Author: shiwen
 * Date: 2025/4/14 10:56
 * Description:
 */
public enum TaskTypeEnum {

    CLOSE_POSITION("CLOSE_POSITION", "平仓"),
    OPEN_POSITION("OPEN_POSITION", "开仓"),
    SET_STOP_PROFIT_LOSS("SET_STOP_PROFIT_LOSS", "设置止盈止损"),
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
