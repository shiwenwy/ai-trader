package com.dodo.ai_trader.service.enums;

public enum SignalEnum {

    BUY_TO_ENTER("buy_to_enter", "买入开仓"),
    SELL_TO_ENTER("sell_to_enter", "卖出开仓"),
    HOLD("hold", "保持"),
    CLOSE("close", "平仓"),
    WAIT("wait", "等待");

    private String code;

    private String value;

    SignalEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static SignalEnum getByCode(String code) {
        for (SignalEnum item : SignalEnum.values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }
}
