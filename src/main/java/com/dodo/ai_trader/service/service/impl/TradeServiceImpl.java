package com.dodo.ai_trader.service.service.impl;

import com.dodo.ai_trader.service.client.ExchangeClient;
import com.dodo.ai_trader.service.enums.SignalEnum;
import com.dodo.ai_trader.service.enums.TaskTypeEnum;
import com.dodo.ai_trader.service.model.AsyncTask;
import com.dodo.ai_trader.service.model.DecisionResult;
import com.dodo.ai_trader.service.model.Signal;
import com.dodo.ai_trader.service.repository.AsyncTaskRepository;
import com.dodo.ai_trader.service.service.RiskService;
import com.dodo.ai_trader.service.service.TradeService;
import com.dodo.ai_trader.service.utils.IdGenerator;
import com.dodo.ai_trader.service.utils.LogUtil;
import com.dodo.ai_trader.service.utils.TaskUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.Map;

@Service
public class TradeServiceImpl implements TradeService {

    @Autowired
    private Map<String, ExchangeClient> exchangeClientMap;

    @Autowired
    private AsyncTaskRepository asyncTaskRepository;

    @Autowired
    private RiskService riskService;

    @Override
    public void executeStrategy(DecisionResult decisionResult) {
        LogUtil.serviceLog("开始执行策略,decisionResult={}", decisionResult);
        if (decisionResult == null || CollectionUtils.isEmpty(decisionResult.getSignalList())) {
            return;
        }

        for (Signal signal : decisionResult.getSignalList()) {
            LogUtil.serviceLog("处理信号,signal={}", signal);
            boolean beforeTrade = riskService.riskCheckBeforeTrade(decisionResult.getUserId(), decisionResult.getExchange(), signal);
            if (!beforeTrade) {
                continue;
            }
            switch (signal.getSignal()) {
                case BUY_TO_ENTER:
                    LogUtil.serviceLog("BUY_TO_ENTER");
                    handleOpenLongPosition(decisionResult.getUserId(), decisionResult.getExchange(), signal);
                    break;
                case SELL_TO_ENTER:
                    LogUtil.serviceLog("SELL_TO_ENTER");
                    handleOpenShortPosition(decisionResult.getUserId(), decisionResult.getExchange(), signal);
                    break;
                case HOLD:
                    LogUtil.serviceLog("HOLD");
                    break;
                case CLOSE:
                    handleClosePosition(decisionResult.getUserId(), signal.getCoin());
                    break;
                case WAIT:
                    LogUtil.serviceLog("WAIT");
                    break;
                default:
                    LogUtil.serviceLog("UNKNOWN");
            }
        }
    }

    private void handleClosePosition(String userId, String coin) {
        AsyncTask asyncTask = TaskUtil.buildAsyncTask(TaskTypeEnum.CLOSE_POSITION,
                IdGenerator.generateClosePositionTaskId(coin), userId);
        asyncTaskRepository.saveAsyncTask(asyncTask);
    }

    private void handleOpenLongPosition(String userId, String exchange, Signal signal) {
        if (signal.getSignal() != SignalEnum.BUY_TO_ENTER) {
            return;
        }
        if (signal.getEntryPrice().compareTo(signal.getStopLoss()) < 0 || signal.getEntryPrice().compareTo(signal.getProfitTarget()) > 0) {
            return;
        }
        ExchangeClient exchangeClient = exchangeClientMap.get(exchange);
        exchangeClient.openLongPosition(signal);
    }

    private void handleOpenShortPosition(String userId, String exchange, Signal signal) {
        if (signal.getSignal() != SignalEnum.SELL_TO_ENTER) {
            return;
        }
        if (signal.getEntryPrice().compareTo(signal.getStopLoss()) > 0 || signal.getEntryPrice().compareTo(signal.getProfitTarget()) < 0) {
            return;
        }
        ExchangeClient exchangeClient = exchangeClientMap.get(exchange);
        exchangeClient.openShortPosition(signal);
    }
}
