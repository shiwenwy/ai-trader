package com.dodo.ai_trader.service.service;

import com.dodo.ai_trader.service.model.DecisionResult;

public interface TradeService {

    /**
     * 策略执行
     * @param decisionResult
     */
    void executeStrategy(DecisionResult decisionResult);
}
