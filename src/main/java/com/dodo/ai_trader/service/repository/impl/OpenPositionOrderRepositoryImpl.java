package com.dodo.ai_trader.service.repository.impl;

import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import com.dodo.ai_trader.service.enums.PositionOrderStatus;
import com.dodo.ai_trader.service.enums.SideEnum;
import com.dodo.ai_trader.service.mapper.OpenPositionOrderMapper;
import com.dodo.ai_trader.service.mapper.entity.OpenPositionOrderEntity;
import com.dodo.ai_trader.service.model.OpenPositionOrder;
import com.dodo.ai_trader.service.repository.OpenPositionOrderRepository;
import com.dodo.ai_trader.service.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OpenPositionOrderRepositoryImpl implements OpenPositionOrderRepository {

    @Autowired
    private OpenPositionOrderMapper openPositionOrderMapper;

    @Override
    public void save(OpenPositionOrder openPositionOrder) {
        int insert = openPositionOrderMapper.insert(convertToEntity(openPositionOrder));
        AssertUtil.isTrue(insert == 1, ErrorCodeEnum.DATABASE_ERROR, "创建开仓订单信息失败", true);
    }

    @Override
    public OpenPositionOrder findByClientOrderId(String clientOrderId, String exchange) {

        return convertToModel(openPositionOrderMapper.queryByClientOrderId(clientOrderId, exchange));
    }

    @Override
    public List<OpenPositionOrder> queryProcessingOrderList() {
        List<OpenPositionOrderEntity> entityList = openPositionOrderMapper.selectProcessOrder();
        if (entityList != null && !entityList.isEmpty()) {
            return entityList.stream().map(this::convertToModel).toList();
        }
        return null;
    }

    @Override
    public void updateStatus(OpenPositionOrder positionOrder) {
        int i = openPositionOrderMapper.updateStatus(positionOrder.getClientOrderId(), positionOrder.getExchange(),
                positionOrder.getStatus().getCode(), positionOrder.getVersion());
        AssertUtil.isTrue(i == 1, ErrorCodeEnum.DATABASE_ERROR, "更新开仓订单信息失败", true);
        positionOrder.setVersion(positionOrder.getVersion() + 1);
    }

    @Override
    public void updateStopLossClientOrderId(OpenPositionOrder positionOrder) {
        int i = openPositionOrderMapper.updateStopLossClientOrderId(positionOrder.getClientOrderId(), positionOrder.getExchange(),
                positionOrder.getStopLossClientOrderId(), positionOrder.getVersion());
        AssertUtil.isTrue(i == 1, ErrorCodeEnum.DATABASE_ERROR, "更新止损订单id失败", true);
        positionOrder.setVersion(positionOrder.getVersion() + 1);
    }

    @Override
    public void updateProfitClientOrderId(OpenPositionOrder positionOrder) {
        int i = openPositionOrderMapper.updateProfitClientOrderId(positionOrder.getClientOrderId(), positionOrder.getExchange(),
                positionOrder.getProfitClientOrderId(), positionOrder.getVersion());
        AssertUtil.isTrue(i == 1, ErrorCodeEnum.DATABASE_ERROR, "更新止盈订单id失败", true);
        positionOrder.setVersion(positionOrder.getVersion() + 1);
    }

    private OpenPositionOrder convertToModel(OpenPositionOrderEntity openPositionOrderEntity) {
        if (openPositionOrderEntity == null) {
            return null;
        }
        OpenPositionOrder openPositionOrder = new OpenPositionOrder();
        openPositionOrder.setClientOrderId(openPositionOrderEntity.getClientOrderId());
        openPositionOrder.setOrderId(openPositionOrderEntity.getOrderId());
        openPositionOrder.setUserId(openPositionOrderEntity.getUserId());
        openPositionOrder.setExchange(openPositionOrderEntity.getExchange());
        openPositionOrder.setSymbol(openPositionOrderEntity.getSymbol());
        openPositionOrder.setLeverage(openPositionOrderEntity.getLeverage());
        openPositionOrder.setSide(SideEnum.getByCode(openPositionOrderEntity.getSide()));
        openPositionOrder.setType(openPositionOrderEntity.getType());
        openPositionOrder.setTimeInForce(openPositionOrderEntity.getTimeInForce());
        openPositionOrder.setQuantity(openPositionOrderEntity.getQuantity());
        openPositionOrder.setEntryPrice(openPositionOrderEntity.getEntryPrice());
        openPositionOrder.setStopPrice(openPositionOrderEntity.getStopPrice());
        openPositionOrder.setProfitTarget(openPositionOrderEntity.getProfitTarget());
        openPositionOrder.setOrderTime(openPositionOrderEntity.getOrderTime());
        openPositionOrder.setStatus(PositionOrderStatus.getByCode(openPositionOrderEntity.getStatus()));
        openPositionOrder.setStopLossClientOrderId(openPositionOrderEntity.getStopLossClientOrderId());
        openPositionOrder.setProfitClientOrderId(openPositionOrderEntity.getProfitClientOrderId());
        openPositionOrder.setVersion(openPositionOrderEntity.getVersion());
        return openPositionOrder;
    }

    private OpenPositionOrderEntity convertToEntity(OpenPositionOrder openPositionOrder) {
        if (openPositionOrder == null) {
            return null;
        }
        OpenPositionOrderEntity openPositionOrderEntity = new OpenPositionOrderEntity();
        openPositionOrderEntity.setClientOrderId(openPositionOrder.getClientOrderId());
        openPositionOrderEntity.setOrderId(openPositionOrder.getOrderId());
        openPositionOrderEntity.setUserId(openPositionOrder.getUserId());
        openPositionOrderEntity.setExchange(openPositionOrder.getExchange());
        openPositionOrderEntity.setSymbol(openPositionOrder.getSymbol());
        openPositionOrderEntity.setSide(openPositionOrder.getSide().name());
        openPositionOrderEntity.setLeverage(openPositionOrder.getLeverage());
        openPositionOrderEntity.setType(openPositionOrder.getType());
        openPositionOrderEntity.setTimeInForce(openPositionOrder.getTimeInForce());
        openPositionOrderEntity.setQuantity(openPositionOrder.getQuantity());
        openPositionOrderEntity.setEntryPrice(openPositionOrder.getEntryPrice());
        openPositionOrderEntity.setStopPrice(openPositionOrder.getStopPrice());
        openPositionOrderEntity.setProfitTarget(openPositionOrder.getProfitTarget());
        openPositionOrderEntity.setOrderTime(openPositionOrder.getOrderTime());
        openPositionOrderEntity.setStatus(openPositionOrder.getStatus() == null ? null : openPositionOrder.getStatus().getCode());
        openPositionOrderEntity.setStopLossClientOrderId(openPositionOrder.getStopLossClientOrderId());
        openPositionOrderEntity.setProfitClientOrderId(openPositionOrder.getProfitClientOrderId());
        openPositionOrderEntity.setVersion(openPositionOrder.getVersion());
        return openPositionOrderEntity;
    }
}
