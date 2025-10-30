package com.dodo.ai_trader.service.repository.impl;


import cn.hutool.json.JSONUtil;
import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import com.dodo.ai_trader.service.enums.TaskStatusEnum;
import com.dodo.ai_trader.service.enums.TaskTypeEnum;
import com.dodo.ai_trader.service.mapper.AsyncTaskMapper;
import com.dodo.ai_trader.service.mapper.entity.AsyncTaskEntity;
import com.dodo.ai_trader.service.model.AsyncTask;
import com.dodo.ai_trader.service.repository.AsyncTaskRepository;
import com.dodo.ai_trader.service.utils.AssertUtil;
import com.dodo.ai_trader.service.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author: shiwen
 * Date: 2025/4/14 11:21
 * Description:
 */
@Repository
public class AsyncTaskRepositoryImpl implements AsyncTaskRepository {

    @Autowired
    private AsyncTaskMapper asyncTaskMapper;

    @Override
    public void saveAsyncTask(AsyncTask asyncTask) {

        AsyncTask oldAsyncTask = queryAsyncTask(asyncTask.getBizId(), asyncTask.getTaskType(), asyncTask.getShardId());
        if (oldAsyncTask != null) {
            return;
        }

        int i = asyncTaskMapper.saveAsyncTask(convertToEntity(asyncTask));
        AssertUtil.isTrue(i == 1, ErrorCodeEnum.DATABASE_ERROR, "创建异步任务失败", true);
    }

    @Override
    public AsyncTask queryAsyncTask(String bizId, TaskTypeEnum taskType, String shardId) {

        return convertToModel(asyncTaskMapper.queryAsyncTask(bizId, taskType.getCode(), shardId));
    }

    @Override
    public List<AsyncTask> queryExecuteTaskList(List<String> taskTypeList, String shardId, int pageSize) {

        LogUtil.serviceLog("queryExecuteTaskList taskTypeList: {}, shardId: {}, pageSize: {}", taskTypeList, shardId, pageSize);
        List<AsyncTaskEntity> entityList = asyncTaskMapper.queryExecuteTaskList(taskTypeList, shardId, pageSize);
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }
        return entityList.stream().map(this::convertToModel).toList();
    }

    @Override
    public List<AsyncTask> queryAllShardExecuteTaskList(List<String> taskTypeList, int pageSize) {
        LogUtil.serviceLog("queryAllShardExecuteTaskList taskTypeList: {}, pageSize: {}", taskTypeList, pageSize);
        List<AsyncTaskEntity> entityList = asyncTaskMapper.queryAllShardExecuteTaskList(taskTypeList, pageSize);
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }
        return entityList.stream().map(this::convertToModel).toList();
    }

    @Override
    public void updateAsyncTaskStatus(AsyncTask asyncTask, TaskStatusEnum afterStatus) {

        int i = asyncTaskMapper.updateAsyncTaskStatus(asyncTask.getBizId(), asyncTask.getTaskType().getCode(), asyncTask.getShardId(),
                asyncTask.getTaskStatus().getCode(), afterStatus.getCode(), asyncTask.getVersion());
        AssertUtil.isTrue(i == 1, ErrorCodeEnum.DATABASE_ERROR, "更新异步任务状态失败", true);
        asyncTask.setVersion(asyncTask.getVersion() + 1);
        asyncTask.setTaskStatus(afterStatus);
    }

    @Override
    public void updateExecuteCountAndNextExecuteTime(String bizId, TaskTypeEnum taskType, String shardId,
                                                     int executeCount, Date nextExecuteTime, int version) {
        int i = asyncTaskMapper.updateExecuteCountAndNextExecuteTime(bizId, taskType.getCode(),
                shardId, executeCount, nextExecuteTime, version);
        AssertUtil.isTrue(i == 1, ErrorCodeEnum.DATABASE_ERROR, "更新异步任务执行次数和下次执行时间失败", true);
    }

    @Override
    public List<AsyncTask> queryUserProcessingTask(String userId, TaskTypeEnum taskType, String shardId) {
        List<AsyncTaskEntity> entityList = asyncTaskMapper.queryUserProcessingTask(userId, taskType.getCode(), shardId);
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }
        return entityList.stream().map(this::convertToModel).toList();
    }

    private AsyncTask convertToModel(AsyncTaskEntity asyncTaskEntity) {
        if (asyncTaskEntity == null) {
            return null;
        }
        AsyncTask asyncTask = new AsyncTask();
        asyncTask.setBizId(asyncTaskEntity.getBizId());
        asyncTask.setCreateTime(asyncTaskEntity.getCreateTime());
        asyncTask.setUpdateTime(asyncTaskEntity.getUpdateTime());
        asyncTask.setTaskType(TaskTypeEnum.getByCode(asyncTaskEntity.getTaskType()));
        asyncTask.setUserId(asyncTaskEntity.getUserId());
        asyncTask.setShardId(asyncTaskEntity.getShardId());
        asyncTask.setTaskStatus(TaskStatusEnum.getByCode(asyncTaskEntity.getTaskStatus()));
        asyncTask.setNextExecuteTime(asyncTaskEntity.getNextExecuteTime());
        asyncTask.setExecuteCount(asyncTaskEntity.getExecuteCount());
        asyncTask.setExtInfo(StringUtils.isBlank(asyncTaskEntity.getExtInfo()) ?
                null : JSONUtil.toBean(asyncTaskEntity.getExtInfo(), Map.class));
        asyncTask.setVersion(asyncTaskEntity.getVersion());
        return asyncTask;
    }

    private AsyncTaskEntity convertToEntity(AsyncTask asyncTask) {
        if (asyncTask == null) {
            return null;
        }
        AsyncTaskEntity asyncTaskEntity = new AsyncTaskEntity();
        asyncTaskEntity.setBizId(asyncTask.getBizId());
        asyncTaskEntity.setTaskType(asyncTask.getTaskType() == null ? null : asyncTask.getTaskType().getCode());
        asyncTaskEntity.setUserId(asyncTask.getUserId());
        asyncTaskEntity.setShardId(asyncTask.getShardId());
        asyncTaskEntity.setTaskStatus(asyncTask.getTaskStatus() == null ? null : asyncTask.getTaskStatus().getCode());
        asyncTaskEntity.setNextExecuteTime(asyncTask.getNextExecuteTime());
        asyncTaskEntity.setExecuteCount(asyncTask.getExecuteCount());
        asyncTaskEntity.setExtInfo(CollectionUtils.isEmpty(asyncTask.getExtInfo()) ?
                null : JSONUtil.toJsonStr(asyncTask.getExtInfo()));
        asyncTaskEntity.setVersion(asyncTask.getVersion());
        return asyncTaskEntity;
    }

}
