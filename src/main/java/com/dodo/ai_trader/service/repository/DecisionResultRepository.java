package com.dodo.ai_trader.service.repository;

import com.dodo.ai_trader.service.model.DecisionResult;

import java.util.List;

public interface DecisionResultRepository {

    void save(DecisionResult decisionResult);

    List<DecisionResult> getLastDecisionResultList(String userId, String exchange, Integer limit);
}
