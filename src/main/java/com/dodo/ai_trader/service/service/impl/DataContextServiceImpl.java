package com.dodo.ai_trader.service.service.impl;

import com.dodo.ai_trader.service.Indicator.*;
import com.dodo.ai_trader.service.client.ExchangeClient;
import com.dodo.ai_trader.service.enums.ExchangeIntervalEnum;
import com.dodo.ai_trader.service.model.Account;
import com.dodo.ai_trader.service.model.DecisionContext;
import com.dodo.ai_trader.service.model.TokenIndicator;
import com.dodo.ai_trader.service.model.market.ExchangeBalance;
import com.dodo.ai_trader.service.model.market.FundingRate;
import com.dodo.ai_trader.service.model.market.KLine;
import com.dodo.ai_trader.service.repository.AccountRepository;
import com.dodo.ai_trader.service.repository.ExchangeBalanceSnapRepository;
import com.dodo.ai_trader.service.service.DataContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class DataContextServiceImpl implements DataContextService {

    @Autowired
    private Map<String, ExchangeClient> exchangeClientMap;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ExchangeBalanceSnapRepository exchangeBalanceSnapRepository;

    @Override
    public DecisionContext getDecisionContext(String userId, String exchange) {
        TokenIndicator btc = getTokenIndicator(exchange, "BTC");
        sleep(1000);
        TokenIndicator eth = getTokenIndicator(exchange, "ETH");
        sleep(1000);
        TokenIndicator bnb = getTokenIndicator(exchange, "BNB");
        sleep(1000);
        TokenIndicator sol = getTokenIndicator(exchange, "SOL");

        DecisionContext decisionContext = new DecisionContext();
        decisionContext.setUserId(userId);
        decisionContext.setExchange(exchange);

        Account account = accountRepository.getAccount(userId, exchange, "USDT");
        decisionContext.setInitTotalBalance(account.getBalance());

        List<Double> lastTotalEquity = exchangeBalanceSnapRepository.getLastTotalEquity(userId, exchange);
        decisionContext.setSharpeRatio(SharpeRatioCalculator.calculateSharpeRatio(lastTotalEquity));

        decisionContext.setTokenIndicatorsMap(Map.of(
                "BTC", btc,
                "ETH", eth,
                "BNB", bnb,
                "SOL", sol
        ));

        ExchangeClient binance = exchangeClientMap.get("binance");
        ExchangeBalance balance = binance.getBalance();

        decisionContext.setAvailableBalance(balance.getAvailableBalance());
        decisionContext.setTotalEquity(balance.calculateTotalEquity());

        // 计算回报率的优化版本
        BigDecimal totalEquity = balance.calculateTotalEquity();
        BigDecimal initBalance = decisionContext.getInitTotalBalance();

        // 防止除零异常
        if (initBalance != null && initBalance.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal returnPct = totalEquity
                    .divide(initBalance, 8, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            decisionContext.setReturnPct(returnPct.doubleValue());
        } else {
            decisionContext.setReturnPct(0.0);
        }

        return decisionContext;
    }

    private TokenIndicator getTokenIndicator(String exchange, String token) {
        ExchangeClient exchangeClient = exchangeClientMap.get(exchange);
        BigDecimal currentPrice = exchangeClient.getCurrentPrice(token);
        BigDecimal openInterest = exchangeClient.getOpenInterest(token);
        BigDecimal avgOpenInterest = exchangeClient.getAvgOpenInterest(token);
        List<FundingRate> fundingRate = exchangeClient.getLastFundingRate(token);
        List<KLine> kLine15m = exchangeClient.getFuturesKLine(token, ExchangeIntervalEnum.INTERVAL_15m, 50);
        List<KLine> kLine1h = exchangeClient.getFuturesKLine(token, ExchangeIntervalEnum.INTERVAL_1h, 50);
        List<KLine> kLine4h = exchangeClient.getFuturesKLine(token, ExchangeIntervalEnum.INTERVAL_4h, 50);

        TokenIndicator tokenIndicator = new TokenIndicator();
        tokenIndicator.setToken(token);
        tokenIndicator.setCurrentPrice(currentPrice);
        tokenIndicator.setCurrentOpenInterest(openInterest);
        tokenIndicator.setCurrentAvgOpenInterest(avgOpenInterest);
        tokenIndicator.setCurrentFundingRate(fundingRate.get(0).getFundingRate());
        tokenIndicator.setMiddlePriceList(MiddlePriceCalculator.calculateMiddlePrice(kLine15m));
        tokenIndicator.setEma20List15m(EmaCalculator.calculateEmaList(kLine15m, 20));
        tokenIndicator.setMacdList15m(MacdCalculator.calculateMacdList(kLine15m, 12, 26, 9));
        tokenIndicator.setAtrList15m(AtrCalculator.calculateAtrList(kLine15m, 14));
        tokenIndicator.setRsi7List15m(RsiCalculator.calculateRsiList(kLine15m, 7));
        tokenIndicator.setRsi14List15m(RsiCalculator.calculateRsiList(kLine15m, 14));
        tokenIndicator.setEma20List1h(EmaCalculator.calculateEmaList(kLine1h, 20));
        tokenIndicator.setMacdList1h(MacdCalculator.calculateMacdList(kLine1h, 12, 26, 9));
        tokenIndicator.setAtrList1h(AtrCalculator.calculateAtrList(kLine1h, 14));
        tokenIndicator.setRsi7List1h(RsiCalculator.calculateRsiList(kLine1h, 7));
        tokenIndicator.setRsi14List1h(RsiCalculator.calculateRsiList(kLine1h, 14));
        tokenIndicator.setEma20List4h(EmaCalculator.calculateEmaList(kLine4h, 20));
        tokenIndicator.setMacdList4h(MacdCalculator.calculateMacdList(kLine4h, 12, 26, 9));
        tokenIndicator.setAtrList4h(AtrCalculator.calculateAtrList(kLine4h, 14));
        tokenIndicator.setRsi7List4h(RsiCalculator.calculateRsiList(kLine4h, 7));
        tokenIndicator.setRsi14List4h(RsiCalculator.calculateRsiList(kLine4h, 14));
        return tokenIndicator;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
