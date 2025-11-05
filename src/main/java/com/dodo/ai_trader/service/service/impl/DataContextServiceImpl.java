package com.dodo.ai_trader.service.service.impl;

import com.dodo.ai_trader.service.model.DecisionContext;
import com.dodo.ai_trader.service.service.DataContextService;
import org.springframework.stereotype.Service;

@Service
public class DataContextServiceImpl implements DataContextService {
    @Override
    public DecisionContext getDecisionContext(String userId, String exchange) {
        return null;
    }
}
