package com.dodo.ai_trader.service.model;

import com.dodo.ai_trader.service.enums.TaskStatusEnum;
import com.dodo.ai_trader.service.enums.TaskTypeEnum;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: shiwen
 * Date: 2025/4/14 10:55
 * Description:
 */
@Data
@ToString
public class AsyncTask implements Serializable {

    private static final long serialVersionUID = -4110832081340883799L;

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
    private TaskTypeEnum taskType;
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
    private TaskStatusEnum taskStatus;
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
    private Map<String, Object> extInfo;
    /**
     * 版本号
     */
    private Integer version;

    public void addExtInfo(String key, Object value) {
        if (extInfo == null) {
            extInfo = new HashMap<>();
        }
        extInfo.put(key, value);
    }

    public Object getExtInfo(String key) {
        if (extInfo == null) {
            return null;
        }
        return extInfo.get(key);
    }

    public boolean canExecute() {
        return TaskStatusEnum.INIT.equals(taskStatus) || TaskStatusEnum.ERROR.equals(taskStatus);
    }

    public void calculateNextExecuteTime() {
        this.executeCount = this.executeCount + 1;
        if (executeCount < 3) {
            this.nextExecuteTime = DateUtils.addMinutes(this.nextExecuteTime, executeCount);
        } else if (executeCount < 10) {
            this.nextExecuteTime = DateUtils.addMinutes(this.nextExecuteTime, 2 * executeCount);
        } else {
            this.nextExecuteTime = DateUtils.addMinutes(this.nextExecuteTime, 30);
        }
    }
}
