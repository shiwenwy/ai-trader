package com.dodo.ai_trader.biz.taskhandler.impl;

import com.dodo.ai_trader.biz.taskhandler.TaskHandler;
import com.dodo.ai_trader.service.client.ExchangeClient;
import com.dodo.ai_trader.service.model.AsyncTask;
import com.dodo.ai_trader.service.model.Signal;
import com.dodo.ai_trader.service.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("OPEN_POSITION")
public class OpenPositionTaskHandler implements TaskHandler {

    @Autowired
    private Map<String, ExchangeClient> exchangeClientMap;

    @Override
    public void handleAsyncTask(AsyncTask asyncTask) {
        Signal signal = (Signal) asyncTask.getExtInfo("signal");
        String exchange = (String) asyncTask.getExtInfo("exchange");
        ExchangeClient exchangeClient = exchangeClientMap.get(exchange);
        switch (signal.getSignal()) {
            case BUY_TO_ENTER:
                LogUtil.serviceLog("BUY_TO_ENTER");
                exchangeClient.openLongPosition(asyncTask.getBizId(), asyncTask.getUserId(), signal);
                break;
            case SELL_TO_ENTER:
                LogUtil.serviceLog("SELL_TO_ENTER");
                exchangeClient.openShortPosition(asyncTask.getBizId(), asyncTask.getUserId(), signal);
                break;
            default:
                LogUtil.serviceLog("UNKNOWN");
        }
    }
}
