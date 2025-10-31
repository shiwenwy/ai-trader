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
     * 标的代码
     */
    private String symbol;
    /**
     * 持仓数量
     */
    private Double quantity;
    /**
     * 入场价格
     */
    private Double entry_price;
    /**
     * 当前价格
     */
    private Double current_price;
    /**
     * 强平价格
     */
    private Double liquidation_price;
    /**
     * 未实现盈亏
     */
    private Double unrealized_pnl;
    /**
     * 杠杆倍数
     */
    private Integer leverage;
    /**
     * 止盈目标
     */
    private Double profit_target;
    /**
     * 止损位
     */
    private Double stop_loss;
    /**
     * 策略失效条件
     */
    private String invalidation_condition;
    /**
     * 信心指数
     */
    private Double confidence;
    /**
     * 风险金额（USD）
     */
    private Double risk_usd;
    /**
     * 名义价值（USD）
     */
    private Double notional_usd;
}
