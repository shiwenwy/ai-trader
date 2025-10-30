package com.dodo.ai_trader.biz.task;



import com.dodo.ai_trader.biz.taskhandler.TaskHandler;
import com.dodo.ai_trader.service.enums.TaskStatusEnum;
import com.dodo.ai_trader.service.model.AsyncTask;
import com.dodo.ai_trader.service.repository.AsyncTaskRepository;
import com.dodo.ai_trader.service.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class AsyncTaskSchedule {

    @Autowired
    private AsyncTaskRepository asyncTaskRepository;

    @Autowired
    private Map<String, TaskHandler> taskHandlerMap;

    private final List<String> taskTypeList = List.of("TOKEN_INFO_SYNC");

    private final List<String> allExecuteTaskTypeList = List.of("TEST_TASK");

    @Scheduled(cron = "0/10 * * * * ?")
    public void executeAllAsyncTask() {

        System.out.println("Start executing scheduled common task");

        List<AsyncTask> asyncTasks = asyncTaskRepository.queryAllShardExecuteTaskList(allExecuteTaskTypeList,  10);

        if (asyncTasks == null || asyncTasks.isEmpty()) {
            LogUtil.bizLog("no task need execute");
            return;
        }

        asyncTasks.forEach(this::processAsyncTask);

        System.out.println("End executing scheduled common task");
    }


    private void processAsyncTask(AsyncTask asyncTask) {
        try {
            if (!asyncTask.canExecute()) {
                return;
            }
            asyncTaskRepository.updateAsyncTaskStatus(asyncTask, TaskStatusEnum.PROCESSING);

            TaskHandler taskHandler = taskHandlerMap.get(asyncTask.getTaskType().getCode());

            if (taskHandler != null) {
                taskHandler.handleAsyncTask(asyncTask);
            } else {
                LogUtil.error("AsyncTaskMQConsumer processAsyncTask taskHandler is null, taskType: {}", asyncTask.getTaskType());
                asyncTaskRepository.updateAsyncTaskStatus(asyncTask, TaskStatusEnum.FAIL);
                return;
            }

            if (asyncTask.getTaskStatus() == TaskStatusEnum.FAIL) {
                asyncTaskRepository.updateAsyncTaskStatus(asyncTask, TaskStatusEnum.FAIL);
                return;
            }

            asyncTaskRepository.updateAsyncTaskStatus(asyncTask, TaskStatusEnum.SUCCESS);
        } catch (Throwable throwable) {
            LogUtil.error("AsyncTaskMQConsumer processAsyncTask error: {}", throwable);
            if (asyncTask.getTaskStatus() == TaskStatusEnum.PROCESSING) {
                asyncTaskRepository.updateAsyncTaskStatus(asyncTask, TaskStatusEnum.ERROR);
                asyncTask.calculateNextExecuteTime();
                asyncTaskRepository.updateExecuteCountAndNextExecuteTime(asyncTask.getBizId(),
                        asyncTask.getTaskType(), asyncTask.getShardId(), asyncTask.getExecuteCount(),
                        asyncTask.getNextExecuteTime(), asyncTask.getVersion());
            }
        }
    }
}
