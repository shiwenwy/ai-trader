package com.dodo.ai_trader.service.enums;

public enum SideEnum {

    LONG("LONG", "多"),
    SHORT("SHORT", "空");

    private String code;

    private String value;

    private SideEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static SideEnum getByCode(String code) {
        for (SideEnum sideEnum : values()) {
            if (sideEnum.code.equals(code)) {
                return sideEnum;
            }
        }
        return null;
    }
}
