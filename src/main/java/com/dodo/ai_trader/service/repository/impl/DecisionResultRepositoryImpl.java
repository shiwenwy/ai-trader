package com.dodo.ai_trader.service.repository.impl;

import com.alibaba.fastjson2.JSON;
import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import com.dodo.ai_trader.service.mapper.DecisionResultMapper;
import com.dodo.ai_trader.service.mapper.entity.DecisionResultEntity;
import com.dodo.ai_trader.service.model.DecisionResult;
import com.dodo.ai_trader.service.model.Signal;
import com.dodo.ai_trader.service.repository.DecisionResultRepository;
import com.dodo.ai_trader.service.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DecisionResultRepositoryImpl implements DecisionResultRepository {

    @Autowired
    private DecisionResultMapper decisionResultMapper;

    @Override
    public void save(DecisionResult decisionResult) {
        int insert = decisionResultMapper.insert(buildNewEntity(decisionResult));
        AssertUtil.isTrue(insert > 0, ErrorCodeEnum.DATABASE_ERROR, "保存决策结果失败", true);
    }

    @Override
    public List<DecisionResult> getLastDecisionResultList(String userId, String exchange, Integer limit) {
        List<DecisionResultEntity> lastDecisionResult = decisionResultMapper.getLastDecisionResult(userId, exchange, limit);
        if (lastDecisionResult == null || lastDecisionResult.isEmpty()) {
            return null;
        }
        return lastDecisionResult.stream().map(this::convertToModel).toList();
    }

    private DecisionResult convertToModel(DecisionResultEntity entity) {
        if (entity == null) {
            return null;
        }
        DecisionResult decisionResult = new DecisionResult();
        decisionResult.setUserId(entity.getUserId());
        decisionResult.setExchange(entity.getExchange());
        decisionResult.setThinking(entity.getThinking());
        decisionResult.setSignalList(JSON.parseArray(entity.getSignalList(), Signal.class));
        return decisionResult;
    }

    private DecisionResultEntity buildNewEntity(DecisionResult decisionResult) {
        DecisionResultEntity entity = new DecisionResultEntity();
        entity.setUserId(decisionResult.getUserId());
        entity.setExchange(decisionResult.getExchange());
        entity.setThinking(decisionResult.getThinking());
        entity.setSignalList(JSON.toJSONString(decisionResult.getSignalList()));
        entity.setTimestamp(System.currentTimeMillis());
        return entity;
    }
}
