package com.dodo.ai_trader.service.service.impl;

import com.dodo.ai_trader.service.model.DecisionContext;
import com.dodo.ai_trader.service.model.Signal;
import com.dodo.ai_trader.service.service.DecisionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DecisionServiceImpl implements DecisionService {
    @Override
    public List<Signal> decide(DecisionContext decisionContext) {
        return List.of();
    }
}
