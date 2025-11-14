package com.dodo.ai_trader.service.client;

import com.dodo.ai_trader.service.enums.ExchangeIntervalEnum;
import com.dodo.ai_trader.service.enums.PositionOrderStatus;
import com.dodo.ai_trader.service.enums.SideEnum;
import com.dodo.ai_trader.service.model.OpenPositionOrder;
import com.dodo.ai_trader.service.model.Signal;
import com.dodo.ai_trader.service.model.market.ExchangeBalance;
import com.dodo.ai_trader.service.model.market.ExchangePosition;
import com.dodo.ai_trader.service.model.market.FundingRate;
import com.dodo.ai_trader.service.model.market.KLine;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeClient {


    BigDecimal getCurrentPrice(String symbol);

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
     * 获取合约平均未平仓合约数
     * @param symbol
     * @return
     */
    BigDecimal getAvgOpenInterest(String symbol);

    /**
     * 获取合约资金费率
     * @param symbol
     * @return
     */
    List<FundingRate> getLastFundingRate(String symbol);

    /**
     * 获取交易所账户余额
     * @return
     */
    ExchangeBalance getBalance();

    /**
     * 获取交易所账户持仓
     * @param symbol
     * @return
     */
    List<ExchangePosition> getPosition(String symbol);

    /**
     * 开多仓位
     * @param bizId
     * @param userId
     * @param signal
     */
    void openLongPosition(String bizId, String userId, Signal signal);

    /**
     * 开空仓位
     * @param bizId
     * @param userId
     * @param signal
     */
    void openShortPosition(String bizId, String userId, Signal signal);

    /**
     * 获取仓位订单状态
     * @param openPositionOrder
     * @return
     */
    PositionOrderStatus getPositionOrderStatus(OpenPositionOrder openPositionOrder);

    /**
     * 设置止损价
     * @param userId
     * @param symbol
     * @param side
     * @param stopLoss
     */
    void setStopLoss(String userId, String symbol, SideEnum side, BigDecimal stopLoss);

    /**
     * 设置止盈价
     * @param userId
     * @param symbol
     * @param side
     * @param takeProfit
     */
    void setTakeProfit(String userId, String symbol, SideEnum side, BigDecimal takeProfit);
}
