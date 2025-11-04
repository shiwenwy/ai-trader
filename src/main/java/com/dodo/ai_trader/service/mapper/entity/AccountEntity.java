package com.dodo.ai_trader.service.mapper.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户
 */
@Data
@ToString
public class AccountEntity implements Serializable {

    private static final long serialVersionUID = 5991168817752531685L;

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
     * 账户名称
     */
    private String accountName;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 卡id
     */
    private String cardId;

    /**
     * 币种
     */
    private String currency;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal frozenBalance;

    /**
     * 可用余额
     */
    private BigDecimal availableBalance;

    /**
     * 状态
     */
    private String status;

    /**
     * 版本号
     */
    private Long version;
}
