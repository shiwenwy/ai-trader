package com.dodo.ai_trader.service.model;

import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

/**
 * 通用仓位
 */
@Data
@ToString
public class CommonPosition implements Serializable {

    private static final long serialVersionUID = -4770193413944831966L;
    /**
     * 标的代码 (如 BTC, ETH 等)
     */
    private String symbol;

    /**
     * 仓位方向 (LONG, SHORT)
     */
    private String side;

    /**
     * 持仓数量
     */
    private Double quantity;

    /**
     * 入场价格
     */
    private Double entryPrice;

    /**
     * 当前价格
     */
    private Double currentPrice;

    /**
     * 强平价格
     */
    private Double liquidationPrice;

    /**
     * 未实现盈亏
     */
    private Double unrealizedPnl;

    /**
     * 杠杆倍数
     */
    private Integer leverage;

    /**
     * 止盈目标
     */
    private Double profitTarget;

    /**
     * 止损位
     */
    private Double stopLoss;

    /**
     * 策略失效条件
     */
    private String invalidationCondition;

    /**
     * 信心指数 (0-1)
     */
    private Double confidence;

    /**
     * 风险金额（USD）
     */
    private Double riskUsd;

    /**
     * 名义价值（USD）
     */
    private Double notionalUsd;

}
