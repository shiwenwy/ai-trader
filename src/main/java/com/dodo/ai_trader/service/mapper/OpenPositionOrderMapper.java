package com.dodo.ai_trader.service.mapper;

import com.dodo.ai_trader.service.mapper.entity.OpenPositionOrderEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OpenPositionOrderMapper {

    int insert(OpenPositionOrderEntity record);

    List<OpenPositionOrderEntity> selectProcessOrder();

    OpenPositionOrderEntity queryByClientOrderId(@Param("clientOrderId") String clientOrderId, @Param("exchange") String exchange);

    int updateStatus(@Param("clientOrderId") String clientOrderId, @Param("exchange") String exchange,
                     @Param("status") String status, @Param("version") Integer version);

    int updateStopLossClientOrderId(@Param("clientOrderId") String clientOrderId, @Param("exchange") String exchange,
                                    @Param("stopLossClientOrderId") String stopLossClientOrderId, @Param("version") Integer version);

    int updateProfitClientOrderId(@Param("clientOrderId") String clientOrderId, @Param("exchange") String exchange,
                                  @Param("profitClientOrderId") String profitClientOrderId, @Param("version") Integer version);
}
