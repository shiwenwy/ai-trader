package com.dodo.ai_trader.service.model;

import com.dodo.ai_trader.service.model.market.MacdResult;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
public class TokenIndicator implements Serializable {

    private static final long serialVersionUID = 6547619323183806681L;

    private String token;

    private BigDecimal currentPrice;

    private BigDecimal currentOpenInterest;

    private BigDecimal currentAvgOpenInterest;

    private BigDecimal currentFundingRate;

    private List<BigDecimal> middlePriceList;

    private List<BigDecimal> ema20List15m;

    private List<MacdResult> macdList15m;

    private List<BigDecimal> atrList15m;

    private List<BigDecimal> rsi7List15m;

    private List<BigDecimal> rsi14List15m;

    private List<BigDecimal> ema20List1h;

    private List<MacdResult> macdList1h;

    private List<BigDecimal> atrList1h;

    private List<BigDecimal> rsi7List1h;

    private List<BigDecimal> rsi14List1h;

    private List<BigDecimal> ema20List4h;

    private List<MacdResult> macdList4h;

    private List<BigDecimal> atrList4h;

    private List<BigDecimal> rsi7List4h;

    private List<BigDecimal> rsi14List4h;
}
