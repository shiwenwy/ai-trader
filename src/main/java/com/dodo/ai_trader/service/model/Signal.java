package com.dodo.ai_trader.service.model;

import com.dodo.ai_trader.service.enums.SignalEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class Signal implements Serializable {

    private static final long serialVersionUID = -3784412294830740973L;

    /**
     * 交易币种
     */
    private String coin;

    /**
     * 交易信号
     */
    private SignalEnum signal;

    /**
     * 订单价格
     */
    private BigDecimal entryPrice;

    /**
     * 订单数量
     */
    private BigDecimal quantity;

    /**
     * 杠杆倍数
     */
    private Integer leverage;

    /**
     * 获利了结价格
     */
    private BigDecimal profitTarget;

    /**
     * 止损价格
     */
    private BigDecimal stopLoss;

    /**
     * 信心指数
     */
    private Double confidence;

    /**
     * 交易逻辑失效的具体市场信号
     */
    private String invalidationCondition;

    /**
     * 策略执行理由
     */
    private String justification;
}
