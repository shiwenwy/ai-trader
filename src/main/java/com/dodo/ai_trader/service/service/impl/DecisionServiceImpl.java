package com.dodo.ai_trader.service.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.dodo.ai_trader.service.client.ExchangeClient;
import com.dodo.ai_trader.service.config.TradeConfig;
import com.dodo.ai_trader.service.decision.BinanceDecision;
import com.dodo.ai_trader.service.enums.SignalEnum;
import com.dodo.ai_trader.service.model.DecisionContext;
import com.dodo.ai_trader.service.model.DecisionResult;
import com.dodo.ai_trader.service.model.Signal;
import com.dodo.ai_trader.service.model.TokenIndicator;
import com.dodo.ai_trader.service.model.market.MacdResult;
import com.dodo.ai_trader.service.service.DecisionService;
import com.dodo.ai_trader.service.utils.AiResParseUtil;
import com.dodo.ai_trader.service.utils.AmountUtil;
import com.dodo.ai_trader.service.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DecisionServiceImpl implements DecisionService {

    @Autowired
    private TradeConfig tradeConfig;
    @Autowired
    private BinanceDecision binanceDecision;
    @Autowired
    private Map<String, ExchangeClient> exchangeClientMap;

    @Override
    public DecisionResult decide(DecisionContext decisionContext) {

        Map<String, Object> btc = getTokenData(decisionContext.getTokenIndicatorsMap().get("BTC"));
        Map<String, Object> eth = getTokenData(decisionContext.getTokenIndicatorsMap().get("ETH"));
        Map<String, Object> sol = getTokenData(decisionContext.getTokenIndicatorsMap().get("SOL"));
        Map<String, Object> bnb = getTokenData(decisionContext.getTokenIndicatorsMap().get("BNB"));


        Flux<String> decision = binanceDecision.getDecision(100, btc, eth, sol, bnb, decisionContext.getReturnPct(),
                decisionContext.getSharpeRatio(), decisionContext.getAvailableBalance().doubleValue(),
                decisionContext.getTotalEquity().doubleValue(), null, decisionContext.getInitTotalBalance().intValue());

        String result = decision.collectList().block().stream().collect(Collectors.joining());
        LogUtil.monitorLog("decision_result: \n" + result);
        String json = AiResParseUtil.parseAiRes(result).trim();

        List<Signal> signals = parseDecisionJson(json);
        DecisionResult decisionResult = new DecisionResult();
        decisionResult.setUserId(decisionContext.getUserId());
        decisionResult.setExchange(decisionContext.getExchange());
        decisionResult.setThinking(AiResParseUtil.parseAiThought(result));
        decisionResult.setSignalList(signals);
        return decisionResult;
    }

    private List<Signal> parseDecisionJson(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        JSONArray jsonArray = JSONArray.parseArray(json);
        List<Signal> signals = new ArrayList<>(jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Signal signal = new Signal();
            String action = jsonObject.getString("action");
            String symbol = jsonObject.getString("symbol");
            if (StringUtils.isNotBlank(action) && StringUtils.isNotBlank(symbol)) {
                signal.setSignal(SignalEnum.getByCode(action));
                signal.setCoin(symbol);
            } else {
                String signalStr = jsonObject.getString("signal");

                signal.setSignal(SignalEnum.getByCode(signalStr));
                signal.setCoin(jsonObject.getString("coin"));
            }

            signal.setInvalidationCondition(jsonObject.getString("invalidation_condition"));
            signal.setJustification(jsonObject.getString("justification"));
            signal.setConfidence(jsonObject.getDouble("confidence"));

            if (signal.getSignal() == SignalEnum.HOLD || signal.getSignal() == SignalEnum.CLOSE
                    || signal.getSignal() == SignalEnum.WAIT) {

                signals.add(signal);
                continue;
            }

            signal.setEntryPrice(new BigDecimal(jsonObject.getString("entry_price")));
            signal.setQuantity(new BigDecimal(jsonObject.getString("quantity")));
            signal.setLeverage(jsonObject.getInteger("leverage"));
            signal.setProfitTarget(new BigDecimal(jsonObject.getString("profit_target")));
            signal.setStopLoss(new BigDecimal(jsonObject.getString("stop_loss")));

            signals.add(signal);
        }
        return signals;
    }

    private Map<String, Object> getTokenData(TokenIndicator tokenIndicator) {
        Map<String, Object> btcData = new HashMap<>();

        String token = tokenIndicator.getToken().toLowerCase();
        btcData.put(token + "_price", tokenIndicator.getCurrentPrice());
        btcData.put(token + "_oi_latest", tokenIndicator.getCurrentOpenInterest());
        btcData.put(token + "_oi_avg", tokenIndicator.getCurrentAvgOpenInterest());
        btcData.put(token + "_funding_rate", tokenIndicator.getCurrentFundingRate());

        btcData.put(token + "_prices_15m", formatList(tokenIndicator.getMiddlePriceList()));

        btcData.put(token + "_ema20_15m", formatList(tokenIndicator.getEma20List15m()));
        btcData.put(token + "_macd_15m", formatMacdList(tokenIndicator.getMacdList15m()));
        btcData.put(token + "_atr_15m", formatList(tokenIndicator.getAtrList15m()));
        btcData.put(token + "_rsi7_15m", formatList(tokenIndicator.getRsi7List15m()));
        btcData.put(token + "_rsi14_15m", formatList(tokenIndicator.getRsi14List15m()));

        btcData.put(token + "_ema20_1h", formatList(tokenIndicator.getEma20List1h()));
        btcData.put(token + "_macd_1h", formatMacdList(tokenIndicator.getMacdList1h()));
        btcData.put(token + "_atr_1h", formatList(tokenIndicator.getAtrList1h()));
        btcData.put(token + "_rsi7_1h", formatList(tokenIndicator.getRsi7List1h()));
        btcData.put(token + "_rsi14_1h", formatList(tokenIndicator.getRsi14List1h()));

        btcData.put(token + "_ema20_4h", formatList(tokenIndicator.getEma20List4h()));
        btcData.put(token + "_macd_4h", formatMacdList(tokenIndicator.getMacdList4h()));
        btcData.put(token + "_atr_4h", formatList(tokenIndicator.getAtrList4h()));
        btcData.put(token + "_rsi7_4h", formatList(tokenIndicator.getRsi7List4h()));
        btcData.put(token + "_rsi14_4h", formatList(tokenIndicator.getRsi14List4h()));

        return btcData;
    }

    // 格式化普通数值列表
    private String formatList(List<BigDecimal> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.stream()
                .map(amount -> AmountUtil.convertToString(amount))
                .collect(Collectors.joining(", "));
    }

    // 格式化MACD列表
    private String formatMacdList(List<MacdResult> macdList) {
        if (macdList == null || macdList.isEmpty()) {
            return "";
        }
        return macdList.stream()
                .map(MacdResult::getMacdLine)
                .map(amount -> AmountUtil.convertToString(amount))
                .collect(Collectors.joining(", "));
    }

}
