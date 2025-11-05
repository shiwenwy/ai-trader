package com.dodo.ai_trader.service.Indicator;

import com.dodo.ai_trader.service.model.market.KLine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MiddlePriceCalculator {

    public static List<BigDecimal> calculateMiddlePrice(List<KLine> kLineList) {
        List<BigDecimal> middlePrices = new ArrayList<>();
        for (KLine kLine : kLineList) {
            BigDecimal middlePrice = kLine.getOpenPrice().add(kLine.getClosePrice()).divide(new BigDecimal("2"));
            middlePrices.add(middlePrice);
        }
        return middlePrices;
    }
}
