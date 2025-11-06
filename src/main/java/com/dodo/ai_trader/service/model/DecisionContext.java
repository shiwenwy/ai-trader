package com.dodo.ai_trader.service.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
@ToString
public class DecisionContext implements Serializable {

    private static final long serialVersionUID = 4593341933507032710L;

    private String userId;

    private String exchange;

    /**
     * 初始总资产
     */
    private BigDecimal initTotalBalance;

    /**
     * 账户总收益率
     */
    private Double returnPct;

    /**
     * 夏普比率
     */
    private Double sharpeRatio;

    /**
     * 可用余额, 仅计算usdt资产
     */
    private BigDecimal availableBalance;

    /**
     * 账户总余额, 仅计算usdt资产
     */
    private BigDecimal totalEquity;

    private Map<String, TokenIndicator> tokenIndicatorsMap;
}
