package com.dodo.ai_trader.service.Indicator;

import com.dodo.ai_trader.service.model.market.KLine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class EmaCalculator {

    // 计算精度设置
    private static final int CALCULATION_SCALE = 10;
    private static final int RESULT_SCALE = 8;

    /**
     * 计算指数移动平均线(EMA) - 单个值
     * @param kLineList K线数据列表
     * @param period 计算周期
     * @return 最新的EMA值
     */
    public static BigDecimal calculateEma(List<KLine> kLineList, int period) {
        if (kLineList == null || kLineList.isEmpty()) {
            return BigDecimal.ZERO;
        }

        if (kLineList.size() < period) {
            // 数据不足时返回简单移动平均
            return calculateSma(kLineList);
        }

        BigDecimal multiplier = BigDecimal.valueOf(2.0).divide(
                BigDecimal.valueOf(period + 1),
                CALCULATION_SCALE,
                RoundingMode.HALF_UP
        );

        // 使用前period个数据的SMA作为初始EMA
        BigDecimal ema = calculateSma(kLineList.subList(0, period));

        // 从第period个数据开始计算EMA
        for (int i = period; i < kLineList.size(); i++) {
            BigDecimal closePrice = kLineList.get(i).getClosePrice();
            ema = closePrice.multiply(multiplier)
                    .add(ema.multiply(BigDecimal.ONE.subtract(multiplier)))
                    .setScale(CALCULATION_SCALE, RoundingMode.HALF_UP);
        }

        return ema.setScale(RESULT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 计算多个EMA值，返回每个K线对应的EMA值列表
     * @param kLineList K线数据列表
     * @param period 计算周期
     * @return EMA值列表
     */
    public static List<BigDecimal> calculateEmaList(List<KLine> kLineList, int period) {
        List<BigDecimal> emaList = new ArrayList<>();

        if (kLineList == null || kLineList.isEmpty()) {
            return emaList;
        }

        BigDecimal multiplier = BigDecimal.valueOf(2.0).divide(
                BigDecimal.valueOf(period + 1),
                CALCULATION_SCALE,
                RoundingMode.HALF_UP
        );

        // 前period-1个数据使用SMA
        for (int i = 0; i < period - 1 && i < kLineList.size(); i++) {
            BigDecimal sma = calculateSma(kLineList.subList(0, i + 1));
            emaList.add(sma.setScale(RESULT_SCALE, RoundingMode.HALF_UP));
        }

        // 从第period个数据开始使用EMA
        if (kLineList.size() >= period) {
            BigDecimal ema = calculateSma(kLineList.subList(0, period));
            emaList.add(ema.setScale(RESULT_SCALE, RoundingMode.HALF_UP));

            for (int i = period; i < kLineList.size(); i++) {
                BigDecimal closePrice = kLineList.get(i).getClosePrice();
                ema = closePrice.multiply(multiplier)
                        .add(ema.multiply(BigDecimal.ONE.subtract(multiplier)))
                        .setScale(CALCULATION_SCALE, RoundingMode.HALF_UP);
                emaList.add(ema.setScale(RESULT_SCALE, RoundingMode.HALF_UP));
            }
        }

        return emaList;
    }

    /**
     * 计算BigDecimal列表的EMA值
     * @param valueList 数值列表
     * @param period 计算周期
     * @return EMA值列表
     */
    public static List<BigDecimal> calculateEmaListWithAmount(List<BigDecimal> valueList, int period) {
        List<BigDecimal> emaList = new ArrayList<>();

        if (valueList == null || valueList.isEmpty()) {
            return emaList;
        }

        BigDecimal multiplier = BigDecimal.valueOf(2.0).divide(
                BigDecimal.valueOf(period + 1),
                CALCULATION_SCALE,
                RoundingMode.HALF_UP
        );

        // 前period-1个数据使用SMA
        for (int i = 0; i < period - 1 && i < valueList.size(); i++) {
            BigDecimal sma = calculateSmaWithAmount(valueList.subList(0, i + 1));
            emaList.add(sma.setScale(RESULT_SCALE, RoundingMode.HALF_UP));
        }

        // 从第period个数据开始使用EMA
        if (valueList.size() >= period) {
            BigDecimal ema = calculateSmaWithAmount(valueList.subList(0, period));
            emaList.add(ema.setScale(RESULT_SCALE, RoundingMode.HALF_UP));

            for (int i = period; i < valueList.size(); i++) {
                BigDecimal value = valueList.get(i);
                ema = value.multiply(multiplier)
                        .add(ema.multiply(BigDecimal.ONE.subtract(multiplier)))
                        .setScale(CALCULATION_SCALE, RoundingMode.HALF_UP);
                emaList.add(ema.setScale(RESULT_SCALE, RoundingMode.HALF_UP));
            }
        }

        return emaList;
    }

    /**
     * 计算简单移动平均(SMA)
     * @param kLineList K线数据列表
     * @return SMA值
     */
    private static BigDecimal calculateSma(List<KLine> kLineList) {
        if (kLineList == null || kLineList.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (KLine kLine : kLineList) {
            sum = sum.add(kLine.getClosePrice());
        }

        return sum.divide(BigDecimal.valueOf(kLineList.size()), CALCULATION_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 计算BigDecimal列表的简单移动平均(SMA)
     * @param valueList 数值列表
     * @return SMA值
     */
    private static BigDecimal calculateSmaWithAmount(List<BigDecimal> valueList) {
        if (valueList == null || valueList.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : valueList) {
            sum = sum.add(value);
        }

        return sum.divide(BigDecimal.valueOf(valueList.size()), CALCULATION_SCALE, RoundingMode.HALF_UP);
    }
}