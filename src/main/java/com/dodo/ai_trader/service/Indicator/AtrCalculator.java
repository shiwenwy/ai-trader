package com.dodo.ai_trader.service.Indicator;

import com.dodo.ai_trader.service.model.market.KLine;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class AtrCalculator {

    private static final int CALCULATION_SCALE = 10;
    private static final int RESULT_SCALE = 8;

    /**
     * 计算ATR指标 - 单个值
     */
    public static BigDecimal calculateAtr(List<KLine> kLineList, int period) {
        List<BigDecimal> atrList = calculateAtrList(kLineList, period);
        if (atrList.isEmpty()) {
            return BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP);
        }
        return atrList.get(atrList.size() - 1);
    }

    /**
     * 计算ATR指标序列 - 修正版
     */
    public static List<BigDecimal> calculateAtrList(List<KLine> kLineList, int period) {
        List<BigDecimal> atrList = new ArrayList<>();

        if (kLineList == null || kLineList.size() < period) {
            return atrList;
        }

        // 计算TR序列
        List<BigDecimal> trList = calculateTrList(kLineList);

        if (trList.size() < period) {
            return atrList;
        }

        // 第一个ATR：前period个TR的简单平均
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < period; i++) {
            sum = sum.add(trList.get(i));
        }
        BigDecimal firstAtr = sum.divide(BigDecimal.valueOf(period), CALCULATION_SCALE, RoundingMode.HALF_UP);
        atrList.add(firstAtr.setScale(RESULT_SCALE, RoundingMode.HALF_UP));

        // 后续ATR使用Wilder平滑法: ATR = (前一个ATR × (n-1) + 当前TR) / n
        for (int i = period; i < trList.size(); i++) {
            BigDecimal previousAtr = atrList.get(atrList.size() - 1);
            BigDecimal currentTr = trList.get(i);

            BigDecimal atr = previousAtr.multiply(BigDecimal.valueOf(period - 1))
                    .add(currentTr)
                    .divide(BigDecimal.valueOf(period), CALCULATION_SCALE, RoundingMode.HALF_UP);

            atrList.add(atr.setScale(RESULT_SCALE, RoundingMode.HALF_UP));
        }

        return atrList;
    }

    /**
     * 计算真实波幅(TR)序列 - 修正版
     */
    private static List<BigDecimal> calculateTrList(List<KLine> kLineList) {
        List<BigDecimal> trList = new ArrayList<>();

        if (kLineList == null || kLineList.isEmpty()) {
            return trList;
        }

        // 第一根K线的TR：high - low
        KLine first = kLineList.get(0);
        BigDecimal firstTr = first.getHighPrice().subtract(first.getLowPrice());
        trList.add(firstTr);

        // 后续K线的TR
        for (int i = 1; i < kLineList.size(); i++) {
            KLine current = kLineList.get(i);
            KLine previous = kLineList.get(i - 1);

            BigDecimal method1 = current.getHighPrice().subtract(current.getLowPrice());
            BigDecimal method2 = current.getHighPrice().subtract(previous.getClosePrice()).abs();
            BigDecimal method3 = current.getLowPrice().subtract(previous.getClosePrice()).abs();

            BigDecimal tr = method1.max(method2).max(method3);
            trList.add(tr);
        }

        return trList;
    }
}