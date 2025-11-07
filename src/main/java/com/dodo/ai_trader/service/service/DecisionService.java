package com.dodo.ai_trader.service.service;

import com.dodo.ai_trader.service.model.DecisionContext;
import com.dodo.ai_trader.service.model.DecisionResult;

public interface DecisionService {

    DecisionResult decide(DecisionContext decisionContext);
}
