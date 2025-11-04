package com.dodo.ai_trader.service.mapper.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户流水
 */
@Data
@ToString
public class AccountBillEntity implements Serializable {

    private static final long serialVersionUID = 3544179894440145372L;

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
     * 用户ID
     */
    private String userId;

    /**
     * 卡ID
     */
    private String cardId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 订单时间
     */
    private Date orderTime;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 状态
     */
    private String status;

    /**
     * 订单上下文
     */
    private String context;

    /**
     * 版本号
     */
    private Integer version;
}
