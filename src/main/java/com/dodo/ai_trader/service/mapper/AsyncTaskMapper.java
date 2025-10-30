package com.dodo.ai_trader.service.mapper;

import com.dodo.ai_trader.service.mapper.entity.AsyncTaskEntity;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

/**
 * Author: shiwen
 * Date: 2025/4/14 11:11
 * Description:
 */
public interface AsyncTaskMapper {

    /**
     * 保存异步任务
     * @param asyncTaskEntity
     * @return
     */
    int saveAsyncTask(AsyncTaskEntity asyncTaskEntity);

    /**
     * 根据业务id、任务类型、分片id查询异步任务
     * @param bizId
     * @param taskType
     * @param shardId
     * @return
     */
    AsyncTaskEntity queryAsyncTask(@Param("bizId") String bizId, @Param("taskType") String taskType, @Param("shardId") String shardId);

    /**
     * 查询需要执行的任务列表
     * @param taskTypeList
     * @param shardId
     * @return
     */
    List<AsyncTaskEntity> queryExecuteTaskList(@Param("taskTypeList") List<String> taskTypeList, @Param("shardId") String shardId, @Param("pageSize") int pageSize);

    List<AsyncTaskEntity> queryAllShardExecuteTaskList(@Param("taskTypeList") List<String> taskTypeList, @Param("pageSize") int pageSize);

    /**
     * 更新异步任务状态
     * @param bizId
     * @param taskType
     * @param shardId
     * @param beforeStatus
     * @param afterStatus
     * @param version
     * @return
     */
    int updateAsyncTaskStatus(@Param("bizId") String bizId, @Param("taskType") String taskType, @Param("shardId") String shardId,
                              @Param("beforeStatus") String beforeStatus, @Param("afterStatus") String afterStatus, @Param("version") int version);

    /**
     * 更新执行次数和下次执行时间
     * @param bizId
     * @param taskType
     * @param shardId
     * @param executeCount
     * @param nextExecuteTime
     * @param version
     * @return
     */
    int updateExecuteCountAndNextExecuteTime(@Param("bizId") String bizId, @Param("taskType") String taskType, @Param("shardId") String shardId,
                                             @Param("executeCount") int executeCount, @Param("nextExecuteTime") Date nextExecuteTime, @Param("version") int version);


    /**
     * 查询用户正在处理的任务
     *
     * @param userId
     * @param taskType
     * @param shardId
     * @return
     */
    List<AsyncTaskEntity> queryUserProcessingTask(@Param("userId") String userId, @Param("taskType") String taskType, @Param("shardId") String shardId);
}
