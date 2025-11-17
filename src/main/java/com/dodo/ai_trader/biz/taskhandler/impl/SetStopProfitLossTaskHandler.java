package com.dodo.ai_trader.biz.taskhandler.impl;

import com.dodo.ai_trader.biz.taskhandler.TaskHandler;
import com.dodo.ai_trader.service.model.AsyncTask;
import org.springframework.stereotype.Component;

@Component("SET_STOP_PROFIT_LOSS")
public class SetStopProfitLossTaskHandler implements TaskHandler {
    @Override
    public void handleAsyncTask(AsyncTask asyncTask) {
        System.out.println("handleAsyncTask: " + asyncTask);
    }
}
