package com.dodo.ai_trader.service.model.market;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 资金费率
 */
@Data
@ToString
public class FundingRate implements Serializable {

    private static final long serialVersionUID = -6797612647348077545L;
    // 资金费率
    private BigDecimal fundingRate;
    // 资金费时间
    private Long fundingTime;
    // 资金费对应标记价格
    private BigDecimal markPrice;
}
