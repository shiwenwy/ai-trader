package com.dodo.ai_trader.service.model.market;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class ExchangeBalance implements Serializable {

    private static final long serialVersionUID = -5688406456781488572L;
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

    public BigDecimal calculateTotalEquity(){
        return totalWalletBalance.add(totalUnrealizedProfit);
    }
}
