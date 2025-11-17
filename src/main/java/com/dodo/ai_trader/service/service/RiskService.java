package com.dodo.ai_trader.service.service;

import com.dodo.ai_trader.service.model.Signal;
import com.dodo.ai_trader.service.model.market.ExchangePosition;

public interface RiskService {

    boolean riskCheckBeforeTrade(String userId, String exchange, Signal signal);

    void riskCheckAfterTrade(String userId, String exchange, ExchangePosition position);
}
