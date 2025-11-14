package com.dodo.ai_trader.service.enums;

public enum PositionOrderStatus {
    PENDING("PENDING", "待处理"),

    FILLED("FILLED", "已成交"),

    CANCELED("CANCELED", "已取消"),

    EXPIRED("EXPIRED", "已过期"),

    // 成交之后，设置了止盈止损才显示完成
    COMPLETED("COMPLETED", "已完成"),
    ;

    private String code;

    private String message;

    PositionOrderStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static PositionOrderStatus getByCode(String code) {
        for (PositionOrderStatus value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
