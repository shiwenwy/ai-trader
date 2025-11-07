package com.dodo.ai_trader.service.mapper;

import com.dodo.ai_trader.service.mapper.entity.DecisionResultEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DecisionResultMapper {

    int insert(DecisionResultEntity record);

    List<DecisionResultEntity> getLastDecisionResult(@Param("userId") String userId, @Param("exchange") String exchange, @Param("limit") Integer limit);
}
