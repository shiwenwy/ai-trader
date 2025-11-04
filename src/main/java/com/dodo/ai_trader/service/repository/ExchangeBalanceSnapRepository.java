package com.dodo.ai_trader.service.repository;

import com.dodo.ai_trader.service.model.ExchangeBalanceSnap;

import java.util.List;

public interface ExchangeBalanceSnapRepository {

    void save(ExchangeBalanceSnap balanceSnap);

    List<Double> getLastTotalEquity(String userId, String exchange);

    ExchangeBalanceSnap getLastExchangeBalanceSnap(String userId, String exchange);
}
