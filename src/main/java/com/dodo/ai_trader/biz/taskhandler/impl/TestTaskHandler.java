package com.dodo.ai_trader.biz.taskhandler.impl;

import com.dodo.ai_trader.biz.taskhandler.TaskHandler;
import com.dodo.ai_trader.service.model.AsyncTask;
import org.springframework.stereotype.Component;

@Component("TEST_TASK")
public class TestTaskHandler implements TaskHandler {
    @Override
    public void handleAsyncTask(AsyncTask asyncTask) {
        System.out.println("handleAsyncTask: " + asyncTask);
    }
}
