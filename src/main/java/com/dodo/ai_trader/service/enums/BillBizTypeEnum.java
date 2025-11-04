package com.dodo.ai_trader.service.enums;

import org.apache.commons.lang3.StringUtils;

public enum BillBizTypeEnum {

    RECHARGE("RECHARGE", "充值"),
    WITHDRAW("WITHDRAW", "提现"),
    ;

    private String code;

    private String desc;

    BillBizTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static BillBizTypeEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (BillBizTypeEnum value : BillBizTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
