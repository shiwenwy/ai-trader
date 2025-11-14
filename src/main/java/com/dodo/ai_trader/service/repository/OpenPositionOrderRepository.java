package com.dodo.ai_trader.service.repository;

import com.dodo.ai_trader.service.model.OpenPositionOrder;

public interface OpenPositionOrderRepository {

    /**
     * 保存开仓订单
     * @param openPositionOrder
     */
    void save(OpenPositionOrder openPositionOrder);

    OpenPositionOrder findByClientOrderId(String clientOrderId, String exchange);
}
