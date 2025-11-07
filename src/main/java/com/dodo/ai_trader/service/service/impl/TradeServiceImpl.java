package com.dodo.ai_trader.service.service.impl;

import com.dodo.ai_trader.service.model.DecisionResult;
import com.dodo.ai_trader.service.service.TradeService;
import org.springframework.stereotype.Service;

@Service
public class TradeServiceImpl implements TradeService {
    @Override
    public void executeStrategy(DecisionResult decisionResult) {
        System.out.println("执行策略...");
    }
}
