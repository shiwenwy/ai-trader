package com.dodo.ai_trader.service.service.impl;

import com.dodo.ai_trader.service.client.ExchangeClient;
import com.dodo.ai_trader.service.enums.SideEnum;
import com.dodo.ai_trader.service.enums.SignalEnum;
import com.dodo.ai_trader.service.enums.TaskTypeEnum;
import com.dodo.ai_trader.service.model.AsyncTask;
import com.dodo.ai_trader.service.model.DecisionResult;
import com.dodo.ai_trader.service.model.Signal;
import com.dodo.ai_trader.service.model.market.ExchangeBalance;
import com.dodo.ai_trader.service.model.market.ExchangePosition;
import com.dodo.ai_trader.service.repository.AsyncTaskRepository;
import com.dodo.ai_trader.service.service.TradeService;
import com.dodo.ai_trader.service.utils.IdGenerator;
import com.dodo.ai_trader.service.utils.LogUtil;
import com.dodo.ai_trader.service.utils.TaskUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class TradeServiceImpl implements TradeService {

    @Autowired
    private Map<String, ExchangeClient> exchangeClientMap;

    @Autowired
    private AsyncTaskRepository asyncTaskRepository;

    @Override
    public void executeStrategy(DecisionResult decisionResult) {
        LogUtil.serviceLog("开始执行策略,decisionResult={}", decisionResult);
        if (decisionResult == null || CollectionUtils.isEmpty(decisionResult.getSignalList())) {
            return;
        }

        for (Signal signal : decisionResult.getSignalList()) {
            LogUtil.serviceLog("处理信号,signal={}", signal);
            boolean beforeTrade = riskCheckBeforeTrade(decisionResult.getUserId(), decisionResult.getExchange(), signal);
            if (!beforeTrade) {
                continue;
            }
            switch (signal.getSignal()) {
                case BUY_TO_ENTER:
                    LogUtil.serviceLog("BUY_TO_ENTER");
                    break;
                case SELL_TO_ENTER:
                    LogUtil.serviceLog("SELL_TO_ENTER");
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

    private boolean riskCheckBeforeTrade(String userId, String exchange, Signal signal) {
        if (signal.getSignal() != SignalEnum.BUY_TO_ENTER && signal.getSignal() != SignalEnum.SELL_TO_ENTER) {
            return true;
        }
        ExchangeClient exchangeClient = exchangeClientMap.get(exchange);
        ExchangeBalance balance = exchangeClient.getBalance();
        if (balance.calculateTotalEquity().compareTo(BigDecimal.ZERO) <= 0) {
            LogUtil.serviceLog("账户总资产不足,请充值");
            return false;
        }
        if (balance.getTotalWalletBalance().multiply(new BigDecimal("0.3")).compareTo(balance.getAvailableBalance()) > 0) {
            LogUtil.serviceLog("账户可用余额低于总余额30%,请充值");
            return false;
        }
        if (signal.getCoin().equals("BTC") && signal.getConfidence() < 0.8) {
            LogUtil.serviceLog("BTC币种信号idence小于0.8,请重新获取");
            return false;
        }
        List<ExchangePosition> position = exchangeClient.getPosition(signal.getCoin());
        if (!CollectionUtils.isEmpty(position)) {

            for (ExchangePosition exchangePosition : position) {
                if ((exchangePosition.getSide() == SideEnum.LONG && signal.getSignal() == SignalEnum.BUY_TO_ENTER)
                        || (exchangePosition.getSide() == SideEnum.SHORT && signal.getSignal() == SignalEnum.SELL_TO_ENTER)) {
                    LogUtil.serviceLog("账户已存在{}币种持仓", signal.getCoin());
                    return false;
                } else {
                    LogUtil.serviceLog("账户已存在{}币种持仓,请平仓", signal.getCoin());
                    handleClosePosition(userId, signal.getCoin());
                    return false;
                }
            }

            if (position.size() >= 3) {
                LogUtil.serviceLog("账户已存在3个币种持仓,不在新增仓位");
                return false;
            }
        }

        return true;
    }

}
