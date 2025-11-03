package com.dodo.ai_trader.service.model.market;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class MacdResult implements Serializable {

    private static final long serialVersionUID = -1281354713501413575L;

    /**
     * MACD线 (DIF)
     */
    private BigDecimal macdLine;
    /**
     * 信号线 ( DEA )
      */
    private BigDecimal signalLine;
    /**
     * 柱状图 ( BAR )
     */
    private BigDecimal histogram;

    public MacdResult(BigDecimal macdLine, BigDecimal signalLine, BigDecimal histogram) {
        this.macdLine = macdLine;
        this.signalLine = signalLine;
        this.histogram = histogram;
    }
}
