package com.dodo.ai_trader.service.service.impl;

import com.dodo.ai_trader.service.client.ExchangeClient;
import com.dodo.ai_trader.service.constant.SystemConstant;
import com.dodo.ai_trader.service.enums.SideEnum;
import com.dodo.ai_trader.service.enums.SignalEnum;
import com.dodo.ai_trader.service.enums.TaskTypeEnum;
import com.dodo.ai_trader.service.model.AsyncTask;
import com.dodo.ai_trader.service.model.DecisionResult;
import com.dodo.ai_trader.service.model.Signal;
import com.dodo.ai_trader.service.model.market.ExchangeBalance;
import com.dodo.ai_trader.service.model.market.ExchangePosition;
import com.dodo.ai_trader.service.repository.AsyncTaskRepository;
import com.dodo.ai_trader.service.repository.DecisionResultRepository;
import com.dodo.ai_trader.service.service.RiskService;
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
public class RiskServiceImpl implements RiskService {

    @Autowired
    private Map<String, ExchangeClient> exchangeClientMap;

    @Autowired
    private AsyncTaskRepository asyncTaskRepository;

    @Autowired
    private DecisionResultRepository decisionResultRepository;

    @Override
    public boolean riskCheckBeforeTrade(String userId, String exchange, Signal signal) {
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
        if (balance.getAvailableBalance().compareTo(signal.getQuantity()) < 0) {
            LogUtil.serviceLog("账户可用余额不足,请充值");
            return false;
        }
        if (signal.getCoin().equals("BTC") && signal.getConfidence() < 0.8) {
            LogUtil.serviceLog("BTC币种信号idence小于0.8,请重新获取");
            return false;
        }

        List<ExchangePosition> position = exchangeClient.getPosition(signal.getCoin());
        if (!CollectionUtils.isEmpty(position)) {

            for (ExchangePosition exchangePosition : position) {
                if (exchangePosition.getSide() == convertSide(signal.getSignal())) {
                    LogUtil.serviceLog("账户已存在{}币种持仓", signal.getCoin());
                    handleSetStopProfit(userId, signal.getCoin(), signal);
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

    @Override
    public void riskCheckAfterTrade(String userId, String exchange, ExchangePosition position) {
        List<DecisionResult> resultList = decisionResultRepository.getLastDecisionResultList(
                SystemConstant.DEFAULT_USER_ID, "binance", 1);
        if (!CollectionUtils.isEmpty(resultList)) {
            DecisionResult decisionResult = resultList.get(0);
            if (handleLastDecisionResult(decisionResult, position)) {
                LogUtil.serviceLog("上次信号已处理,请勿重复处理");
                return;
            }
        }
    }

    private boolean handleLastDecisionResult(DecisionResult decisionResult, ExchangePosition position) {
        if (decisionResult == null || CollectionUtils.isEmpty(decisionResult.getSignalList())
                || !decisionResult.betweenMinute(1)) {
            return false;
        }
        for (Signal signal : decisionResult.getSignalList()) {
            if (signal.getCoin().equals(position.getSymbol()) && convertSide(signal.getSignal()) == position.getSide()) {
                return true;
            }
        }
        return false;
    }


    private SideEnum convertSide(SignalEnum signalEnum) {
        if (signalEnum == SignalEnum.BUY_TO_ENTER) {
            return SideEnum.LONG;
        }
        if (signalEnum == SignalEnum.SELL_TO_ENTER) {
            return SideEnum.SHORT;
        }
        return null;
    }

    private void handleClosePosition(String userId, String coin) {
        AsyncTask asyncTask = TaskUtil.buildAsyncTask(TaskTypeEnum.CLOSE_POSITION,
                IdGenerator.generateClosePositionTaskId(coin), userId);
        asyncTaskRepository.saveAsyncTask(asyncTask);
    }

    private void handleSetStopProfit(String userId, String coin, Signal signal) {
        AsyncTask asyncTask = TaskUtil.buildAsyncTask(TaskTypeEnum.SET_STOP_PROFIT_LOSS,
                IdGenerator.generateSignalTaskId(coin), userId);
        asyncTask.addExtInfo("stopProfit", signal.getProfitTarget());
        asyncTask.addExtInfo("stopLoss", signal.getStopLoss());
        asyncTask.addExtInfo("coin", coin);
        asyncTaskRepository.saveAsyncTask(asyncTask);
    }
}
