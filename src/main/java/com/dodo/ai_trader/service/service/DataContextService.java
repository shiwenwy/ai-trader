package com.dodo.ai_trader.service.service;

import com.dodo.ai_trader.service.model.DecisionContext;

public interface DataContextService {

    DecisionContext getDecisionContext(String userId, String exchange);
}
