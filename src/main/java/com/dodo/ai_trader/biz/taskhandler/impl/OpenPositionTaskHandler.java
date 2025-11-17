package com.dodo.ai_trader.biz.taskhandler.impl;

import com.dodo.ai_trader.biz.taskhandler.TaskHandler;
import com.dodo.ai_trader.service.model.AsyncTask;
import org.springframework.stereotype.Component;

@Component("OPEN_POSITION")
public class OpenPositionTaskHandler implements TaskHandler {
    @Override
    public void handleAsyncTask(AsyncTask asyncTask) {

    }
}
