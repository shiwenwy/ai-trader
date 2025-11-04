package com.dodo.ai_trader.service.mapper;

import com.dodo.ai_trader.service.mapper.entity.ExchangeBalanceSnapEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeBalanceSnapMapper {

    int insert(ExchangeBalanceSnapEntity record);

    List<ExchangeBalanceSnapEntity> getLastBalanceSnapList(@Param("userId") String userId, @Param("exchange") String exchange);
}
