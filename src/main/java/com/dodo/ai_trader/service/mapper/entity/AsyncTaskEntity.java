package com.dodo.ai_trader.service.mapper.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: shiwen
 * Date: 2025/4/14 11:09
 * Description:
 */
@Data
@ToString
public class AsyncTaskEntity implements Serializable {

    private static final long serialVersionUID = 542842385380314280L;
    /**
     * ID
     */
    private Integer id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 业务id
     */
    private String bizId;
    /**
     * 任务类型
     */
    private String taskType;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 分片id
     */
    private String shardId;
    /**
     * 任务状态
     */
    private String taskStatus;
    /**
     * 下次执行时间
     */
    private Date nextExecuteTime;
    /**
     * 执行次数
     */
    private Integer executeCount;
    /**
     * 扩展信息
     */
    private String extInfo;
    /**
     * 版本号
     */
    private Integer version;
}
