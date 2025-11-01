package com.dodo.ai_trader.service.model.market;

import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class KLine implements Serializable {

    private static final long serialVersionUID = 151306655170681484L;

    /** 时间相关字段 */
    // 开盘时间戳(毫秒)
    private Long openTime;
    // 收盘时间戳(毫秒)
    private Long closeTime;

    /** 价格字段 */
    // 开盘价
    private BigDecimal openPrice;
    // 最高价
    private BigDecimal highPrice;
    // 最低价
    private BigDecimal lowPrice;
    // 收盘价
    private BigDecimal closePrice;

    /** 成交量相关字段 */
    // 成交量
    private BigDecimal volume;
    // 成交额
    private BigDecimal quoteAssetVolume;
    // 主动买入成交量
    private BigDecimal takerBuyBaseVolume;
    // 主动买入成交额
    private BigDecimal takerBuyQuoteVolume;
    // 成交笔数
    private Integer tradeNum;
}
