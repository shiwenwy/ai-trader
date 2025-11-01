package com.dodo.ai_trader.service.client;

import com.dodo.ai_trader.service.enums.ExchangeIntervalEnum;
import com.dodo.ai_trader.service.model.market.FundingRate;
import com.dodo.ai_trader.service.model.market.KLine;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeClient {

    /**
     * 获取合约K线数据
     * @param symbol
     * @param interval
     * @param limit
     * @return
     */
    List<KLine> getFuturesKLine(String symbol, ExchangeIntervalEnum interval, Integer limit);

    /**
     * 获取未平仓合约数
     * @param symbol
     * @return
     */
    BigDecimal getOpenInterest(String symbol);

    /**
     * 获取合约资金费率
     * @param symbol
     * @return
     */
    List<FundingRate> getLastFundingRate(String symbol);
}
