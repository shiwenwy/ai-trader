package com.dodo.ai_trader.service.utils;

import java.math.BigDecimal;

/**
 * Author: shiwen
 * Date: 2025/5/13 15:36
 * Description:
 */
public class AmountUtil {

    public static String convertToString(BigDecimal amount) {
        if (amount == null) {
            return "";
        }
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }
        return amount.toPlainString();
    }
}
