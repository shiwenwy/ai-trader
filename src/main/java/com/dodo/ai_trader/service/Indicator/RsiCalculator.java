package com.dodo.ai_trader.service.Indicator;

import com.dodo.ai_trader.service.model.market.KLine;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class RsiCalculator {

    private static final int CALCULATION_SCALE = 10;
    private static final int RESULT_SCALE = 8;

    /**
     * 计算RSI指标 - 单个值
     */
    public static BigDecimal calculateRsi(List<KLine> kLineList, int period) {
        List<BigDecimal> rsiList = calculateRsiList(kLineList, period);
        if (rsiList.isEmpty()) {
            return BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP);
        }
        return rsiList.get(rsiList.size() - 1);
    }

    /**
     * 计算RSI指标序列 - 修正版（使用EMA方法）
     */
    public static List<BigDecimal> calculateRsiList(List<KLine> kLineList, int period) {
        List<BigDecimal> rsiList = new ArrayList<>();

        if (kLineList == null || kLineList.size() <= period) {
            return rsiList;
        }

        List<BigDecimal> gains = new ArrayList<>();
        List<BigDecimal> losses = new ArrayList<>();

        // 计算价格变化
        for (int i = 1; i < kLineList.size(); i++) {
            BigDecimal change = kLineList.get(i).getClosePrice()
                    .subtract(kLineList.get(i - 1).getClosePrice());

            if (change.compareTo(BigDecimal.ZERO) > 0) {
                gains.add(change);
                losses.add(BigDecimal.ZERO);
            } else {
                gains.add(BigDecimal.ZERO);
                losses.add(change.abs());
            }
        }

        // 前period-1个数据无法计算稳定RSI
        for (int i = 0; i < period - 1; i++) {
            rsiList.add(BigDecimal.ZERO);
        }

        // 计算第一个平均收益和平均损失（简单平均）
        BigDecimal avgGain = BigDecimal.ZERO;
        BigDecimal avgLoss = BigDecimal.ZERO;

        for (int i = 0; i < period; i++) {
            avgGain = avgGain.add(gains.get(i));
            avgLoss = avgLoss.add(losses.get(i));
        }

        avgGain = avgGain.divide(BigDecimal.valueOf(period), CALCULATION_SCALE, RoundingMode.HALF_UP);
        avgLoss = avgLoss.divide(BigDecimal.valueOf(period), CALCULATION_SCALE, RoundingMode.HALF_UP);

        // 计算第一个RSI
        BigDecimal firstRsi = calculateRsiFromAvg(avgGain, avgLoss);
        rsiList.add(firstRsi.setScale(RESULT_SCALE, RoundingMode.HALF_UP));

        // 使用EMA方法计算后续RSI值
        BigDecimal alpha = BigDecimal.ONE.divide(BigDecimal.valueOf(period), CALCULATION_SCALE, RoundingMode.HALF_UP);
        BigDecimal oneMinusAlpha = BigDecimal.ONE.subtract(alpha);

        for (int i = period; i < gains.size(); i++) {
            BigDecimal currentGain = gains.get(i);
            BigDecimal currentLoss = losses.get(i);

            // 更新平均收益和损失：EMA方式
            avgGain = currentGain.multiply(alpha)
                    .add(avgGain.multiply(oneMinusAlpha))
                    .setScale(CALCULATION_SCALE, RoundingMode.HALF_UP);

            avgLoss = currentLoss.multiply(alpha)
                    .add(avgLoss.multiply(oneMinusAlpha))
                    .setScale(CALCULATION_SCALE, RoundingMode.HALF_UP);

            BigDecimal rsi = calculateRsiFromAvg(avgGain, avgLoss);
            rsiList.add(rsi.setScale(RESULT_SCALE, RoundingMode.HALF_UP));
        }

        return rsiList;
    }

    /**
     * 根据平均收益和损失计算RSI
     */
    private static BigDecimal calculateRsiFromAvg(BigDecimal avgGain, BigDecimal avgLoss) {
        if (avgLoss.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(100);
        }

        BigDecimal rs = avgGain.divide(avgLoss, CALCULATION_SCALE, RoundingMode.HALF_UP);
        return BigDecimal.valueOf(100).subtract(
                BigDecimal.valueOf(100).divide(
                        BigDecimal.ONE.add(rs),
                        CALCULATION_SCALE,
                        RoundingMode.HALF_UP
                )
        );
    }

    /**
     * 检查数据是否足够计算RSI
     */
    public static boolean isDataSufficient(List<KLine> kLineList, int period) {
        return kLineList != null && kLineList.size() > period;
    }
}