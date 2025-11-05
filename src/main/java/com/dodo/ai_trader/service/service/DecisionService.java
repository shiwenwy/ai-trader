package com.dodo.ai_trader.service.service;

import com.dodo.ai_trader.service.model.DecisionContext;
import com.dodo.ai_trader.service.model.Signal;

import java.util.List;

public interface DecisionService {

    List<Signal> decide(DecisionContext decisionContext);
}
