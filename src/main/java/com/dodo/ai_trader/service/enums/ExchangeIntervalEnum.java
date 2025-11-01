package com.dodo.ai_trader.service.enums;

public enum ExchangeIntervalEnum {

    INTERVAL_1m("1m"),

    INTERVAL_3m("3m"),

    INTERVAL_5m("5m"),

    INTERVAL_15m("15m"),

    INTERVAL_30m("30m"),

    INTERVAL_1h("1h"),

    INTERVAL_2h("2h"),

    INTERVAL_4h("4h"),

    INTERVAL_6h("6h"),

    INTERVAL_8h("8h"),

    INTERVAL_12h("12h"),

    INTERVAL_1d("1d"),

    INTERVAL_3d("3d"),

    INTERVAL_1w("1w"),

    INTERVAL_1M("1M");

    private String value;

    ExchangeIntervalEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
