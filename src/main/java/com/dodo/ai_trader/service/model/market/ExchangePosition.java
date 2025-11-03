package com.dodo.ai_trader.service.model.market;

import com.dodo.ai_trader.service.enums.SideEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class ExchangePosition implements Serializable {

    private static final long serialVersionUID = -2615502902124897632L;

    private String symbol;
    /**
     * 头寸数量，符号代表多空方向, 正数为多，负数为空
     */
    private BigDecimal positionAmt;
    /**
     * 开仓均价
     */
    private BigDecimal entryPrice;
    /**
     * 当前标记价格
     */
    private BigDecimal markPrice;
    /**
     * 持仓未实现盈亏
     */
    private BigDecimal unRealizedProfit;
    /**
     * 当前杠杆倍数
     */
    private Integer leverage;
    /**
     * 参考强平价格
     */
    private BigDecimal liquidationPrice;
    /**
     * 持仓方向
     */
    private SideEnum side;
}
