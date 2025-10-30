package com.dodo.ai_trader.biz.taskhandler;


import com.dodo.ai_trader.service.model.AsyncTask;

/**
 * Author: shiwen
 * Date: 2025/4/14 15:47
 * Description:
 */
public interface TaskHandler {

    /**
     * 处理异步任务
     * @param asyncTask
     */
    void handleAsyncTask(AsyncTask asyncTask);
}
