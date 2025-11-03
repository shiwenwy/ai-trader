package com.dodo.ai_trader.service.model.market;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class BollingerResult implements Serializable {

    private static final long serialVersionUID = -3461650240988951604L;
    /**
     * 上轨
     */
    private BigDecimal upperBand;
    /**
     * 中轨(均线)
     */
    private BigDecimal middleBand;
    /**
     * 下轨
     */
    private BigDecimal lowerBand;

    public BollingerResult(BigDecimal upperBand, BigDecimal middleBand, BigDecimal lowerBand) {
        this.upperBand = upperBand;
        this.middleBand = middleBand;
        this.lowerBand = lowerBand;
    }
}
