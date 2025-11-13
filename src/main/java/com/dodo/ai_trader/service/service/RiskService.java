package com.dodo.ai_trader.service.service;

import com.dodo.ai_trader.service.model.Signal;

public interface RiskService {

    boolean riskCheckBeforeTrade(String userId, String exchange, Signal signal);
}
