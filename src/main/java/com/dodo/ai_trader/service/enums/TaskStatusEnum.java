package com.dodo.ai_trader.service.enums;


import io.micrometer.common.util.StringUtils;

/**
 * Author: shiwen
 * Date: 2025/4/14 10:59
 * Description:
 */
public enum TaskStatusEnum {
    INIT("INIT", "初始化"),
    PROCESSING("PROCESSING", "处理中"),
    SUCCESS("SUCCESS", "成功"),
    FAIL("FAIL", "失败"),
    ERROR("ERROR", "异常"),
    ;

    private String code;
    private String desc;

    TaskStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TaskStatusEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (TaskStatusEnum taskStatus : TaskStatusEnum.values()) {
            if (taskStatus.code.equals(code)) {
                return taskStatus;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
