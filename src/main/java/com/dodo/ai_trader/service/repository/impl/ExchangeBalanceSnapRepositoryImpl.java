package com.dodo.ai_trader.service.repository.impl;

import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import com.dodo.ai_trader.service.mapper.ExchangeBalanceSnapMapper;
import com.dodo.ai_trader.service.mapper.entity.ExchangeBalanceSnapEntity;
import com.dodo.ai_trader.service.model.ExchangeBalanceSnap;
import com.dodo.ai_trader.service.repository.ExchangeBalanceSnapRepository;
import com.dodo.ai_trader.service.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ExchangeBalanceSnapRepositoryImpl implements ExchangeBalanceSnapRepository {

    @Autowired
    private ExchangeBalanceSnapMapper exchangeBalanceSnapMapper;

    @Override
    public void save(ExchangeBalanceSnap balanceSnap) {
        int insert = exchangeBalanceSnapMapper.insert(convertToEntity(balanceSnap));
        AssertUtil.isTrue(insert == 1, ErrorCodeEnum.DATABASE_ERROR, "保存交易所账户快照失败", true);
    }

    @Override
    public List<Double> getLastTotalEquity(String userId, String exchange) {
        List<ExchangeBalanceSnapEntity> snapList = exchangeBalanceSnapMapper.getLastBalanceSnapList(userId, exchange);
        if (snapList == null || snapList.isEmpty()) {
            return null;
        }
        List<Double> totalEquityList = new ArrayList<>();
        for (int i = snapList.size() - 1; i >= 0; i--) {
            totalEquityList.add(snapList.get(i).getTotalEquity().doubleValue());
        }
        return totalEquityList;
    }

    @Override
    public ExchangeBalanceSnap getLastExchangeBalanceSnap(String userId, String exchange) {
        List<ExchangeBalanceSnapEntity> snapList = exchangeBalanceSnapMapper.getLastBalanceSnapList(userId, exchange);
        if (snapList == null || snapList.isEmpty()) {
            return null;
        }
        return convertToModel(snapList.get(0));
    }

    private ExchangeBalanceSnap convertToModel(ExchangeBalanceSnapEntity entity) {
        if (entity == null) {
            return null;
        }
        ExchangeBalanceSnap model = new ExchangeBalanceSnap();
        model.setUserId(entity.getUserId());
        model.setExchange(entity.getExchange());
        model.setTotalEquity(entity.getTotalEquity());
        model.setTotalWalletBalance(entity.getTotalWalletBalance());
        model.setTotalUnrealizedProfit(entity.getTotalUnrealizedProfit());
        model.setAvailableBalance(entity.getAvailableBalance());
        model.setTimestamp(entity.getTimestamp());
        return model;
    }


    private ExchangeBalanceSnapEntity convertToEntity(ExchangeBalanceSnap model) {
        if (model == null) {
            return null;
        }
        ExchangeBalanceSnapEntity entity = new ExchangeBalanceSnapEntity();
        entity.setUserId(model.getUserId());
        entity.setExchange(model.getExchange());
        entity.setTotalEquity(model.getTotalEquity());
        entity.setTotalWalletBalance(model.getTotalWalletBalance());
        entity.setTotalUnrealizedProfit(model.getTotalUnrealizedProfit());
        entity.setAvailableBalance(model.getAvailableBalance());
        entity.setTimestamp(model.getTimestamp());
        return entity;
    }
}
