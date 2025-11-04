package com.dodo.ai_trader.service.mapper.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
public class ExchangeBalanceSnapEntity implements Serializable {

    private static final long serialVersionUID = 4041644968751529394L;

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

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 交易所
     */
    private String exchange;

    /**
     * 账户总资产
     */
    private BigDecimal totalEquity;

    /**
     * 账户总余额, 仅计算usdt资产
     */
    private BigDecimal totalWalletBalance;
    /**
     * 持仓未实现盈亏总额, 仅计算usdt资产
     */
    private BigDecimal totalUnrealizedProfit;
    /**
     * 可用余额, 仅计算usdt资产
     */
    private BigDecimal availableBalance;

    private Long timestamp;
}
