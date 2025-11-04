package com.dodo.ai_trader.service.enums;

import org.apache.commons.lang3.StringUtils;


public enum AccountStatusEnum {

    NORMAL("NORMAL", "正常"),
    FREEZE("FREEZE", "冻结"),
    FREEZE_OUT("FREEZE_OUT", "冻结出账"),
    FREEZE_IN("FREEZE_IN", "冻结入账"),
    ;

    private String code;
    private String desc;

    AccountStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static AccountStatusEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (AccountStatusEnum item : AccountStatusEnum.values()) {
            if (StringUtils.equals(item.getCode(), code)) {
                return item;
            }
        }
        return null;
    }
}
