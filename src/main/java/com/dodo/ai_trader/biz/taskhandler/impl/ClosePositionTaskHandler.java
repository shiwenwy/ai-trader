package com.dodo.ai_trader.biz.taskhandler.impl;

import com.dodo.ai_trader.biz.taskhandler.TaskHandler;
import com.dodo.ai_trader.service.model.AsyncTask;
import org.springframework.stereotype.Component;

@Component("CLOSE_POSITION")
public class ClosePositionTaskHandler implements TaskHandler {
    @Override
    public void handleAsyncTask(AsyncTask asyncTask) {
        System.out.println("handleAsyncTask: " + asyncTask);
    }
}
