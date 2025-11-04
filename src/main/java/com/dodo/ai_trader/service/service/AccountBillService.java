package com.dodo.ai_trader.service.service;

import java.math.BigDecimal;

public interface AccountBillService {

    void recharge(String orderId, String userId, String cardId, BigDecimal amount, String currency);
}
