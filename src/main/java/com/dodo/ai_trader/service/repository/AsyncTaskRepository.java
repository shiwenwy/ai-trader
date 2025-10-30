package com.dodo.ai_trader.service.repository;

import com.dodo.ai_trader.service.enums.TaskStatusEnum;
import com.dodo.ai_trader.service.enums.TaskTypeEnum;
import com.dodo.ai_trader.service.model.AsyncTask;

import java.util.Date;
import java.util.List;

/**
 * Author: shiwen
 * Date: 2025/4/14 11:20
 * Description:
 */
public interface AsyncTaskRepository {

    /**
     * 保存异步任务
     * @param asyncTask
     */
    void saveAsyncTask(AsyncTask asyncTask);

    /**
     * 查询异步任务
     * @param bizId
     * @param taskType
     * @param shardId
     * @return
     */
    AsyncTask queryAsyncTask(String bizId, TaskTypeEnum taskType, String shardId);

    /**
     * 查询需要执行的任务列表
     * @param taskTypeList
     * @param shardId
     * @param pageSize
     * @return
     */
    List<AsyncTask> queryExecuteTaskList(List<String> taskTypeList, String shardId, int pageSize);

    List<AsyncTask> queryAllShardExecuteTaskList(List<String> taskTypeList, int pageSize);

    /**
     * 更新任务状态
     * @param asyncTask
     * @param afterStatus
     */
    void updateAsyncTaskStatus(AsyncTask asyncTask, TaskStatusEnum afterStatus);

    /**
     * 更新任务执行次数和下次执行时间
     * @param bizId
     * @param taskType
     * @param shardId
     * @param executeCount
     * @param nextExecuteTime
     * @param version
     */
    void updateExecuteCountAndNextExecuteTime(String bizId, TaskTypeEnum taskType, String shardId, int executeCount, Date nextExecuteTime, int version);

    /**
     * 查询用户正在处理的任务
     * @param userId
     * @param taskType
     * @param shardId
     * @return
     */
    List<AsyncTask> queryUserProcessingTask(String userId, TaskTypeEnum taskType, String shardId);
}
