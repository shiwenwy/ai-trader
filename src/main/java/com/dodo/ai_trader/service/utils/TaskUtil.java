package com.dodo.ai_trader.service.utils;

import com.dodo.ai_trader.service.enums.TaskStatusEnum;
import com.dodo.ai_trader.service.enums.TaskTypeEnum;
import com.dodo.ai_trader.service.model.AsyncTask;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class TaskUtil {

    public static AsyncTask buildAsyncTask(TaskTypeEnum taskType, String bizId, String userId) {
        AsyncTask asyncTask = new AsyncTask();
        asyncTask.setBizId(bizId);
        asyncTask.setTaskType(taskType);
        asyncTask.setShardId(getShardId(userId));
        asyncTask.setUserId(userId);
        asyncTask.setTaskStatus(TaskStatusEnum.INIT);
        asyncTask.setNextExecuteTime(new Date());
        asyncTask.setExecuteCount(0);
        asyncTask.setExtInfo(null);
        asyncTask.setVersion(0);
        return asyncTask;
    }

    public static String getShardId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return "00";
        }
        int i = Math.abs(userId.hashCode()) % 10;
        if (i >= 0 && i < 10) {
            return "0" + i;
        }
        return i + "";
    }

}
