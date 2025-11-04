package com.dodo.ai_trader.service.enums;

import org.apache.commons.lang3.StringUtils;

public enum BillStatusEnum {

    INIT("INIT", "初始化"),
    SUCCESS("SUCCESS", "成功"),
    CANCEL("CANCEL", "取消"),
    ;

    private String code;

    private String desc;

    BillStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static BillStatusEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (BillStatusEnum value : BillStatusEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
