package com.dodo.ai_trader.service.mapper.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class DecisionResultEntity implements Serializable {

    private static final long serialVersionUID = -2663271509310363249L;

    /**
     * ID
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private String userId;

    private String exchange;

    private String thinking;

    private String signalList;

    private Long timestamp;
}
