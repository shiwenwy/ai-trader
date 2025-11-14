package com.dodo.ai_trader.service.mapper;

import com.dodo.ai_trader.service.mapper.entity.OpenPositionOrderEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OpenPositionOrderMapper {

    int insert(OpenPositionOrderEntity record);

    List<OpenPositionOrderEntity> selectProcessOrder();

    OpenPositionOrderEntity queryByClientOrderId(@Param("clientOrderId") String clientOrderId, @Param("exchange") String exchange);
}
