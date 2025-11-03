package com.dodo.ai_trader.service.Indicator;

import com.dodo.ai_trader.service.model.market.BollingerResult;
import com.dodo.ai_trader.service.model.market.KLine;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class BollingCalculator {

    private static final int CALCULATION_SCALE = 10;
    private static final int RESULT_SCALE = 8;

    // 默认参数
    private static final int DEFAULT_PERIOD = 20;
    private static final BigDecimal DEFAULT_MULTIPLIER = BigDecimal.valueOf(2);

    /**
     * 计算布林带指标 - 单个值（使用默认参数）
     */
    public static BollingerResult calculateBollinger(List<KLine> kLineList) {
        return calculateBollinger(kLineList, DEFAULT_PERIOD, DEFAULT_MULTIPLIER);
    }

    /**
     * 计算布林带指标序列（使用默认参数）
     */
    public static List<BollingerResult> calculateBollingerList(List<KLine> kLineList) {
        return calculateBollingerList(kLineList, DEFAULT_PERIOD, DEFAULT_MULTIPLIER);
    }

    /**
     * 计算布林带指标 - 单个值
     */
    public static BollingerResult calculateBollinger(List<KLine> kLineList, int period, BigDecimal multiplier) {
        List<BollingerResult> results = calculateBollingerList(kLineList, period, multiplier);
        if (results.isEmpty()) {
            return createEmptyResult();
        }
        return results.get(results.size() - 1);
    }

    /**
     * 计算布林带指标序列 - 优化版
     */
    public static List<BollingerResult> calculateBollingerList(List<KLine> kLineList, int period, BigDecimal multiplier) {
        List<BollingerResult> bollingerList = new ArrayList<>();

        if (kLineList == null || kLineList.size() < period) {
            return bollingerList;
        }

        // 使用滑动窗口优化计算
        List<BigDecimal> closePrices = extractClosePrices(kLineList);

        for (int i = period - 1; i < closePrices.size(); i++) {
            List<BigDecimal> window = closePrices.subList(i - period + 1, i + 1);
            BollingerResult result = calculateBollingerForWindow(window, period, multiplier);
            bollingerList.add(result);
        }

        return bollingerList;
    }

    /**
     * 为滑动窗口计算布林带
     */
    private static BollingerResult calculateBollingerForWindow(List<BigDecimal> prices, int period, BigDecimal multiplier) {
        // 计算中轨(SMA)
        BigDecimal middleBand = calculateSma(prices, period);

        // 计算样本标准差
        BigDecimal standardDeviation = calculateSampleStandardDeviation(prices, middleBand, period);

        // 计算上下轨
        BigDecimal upperBand = middleBand.add(standardDeviation.multiply(multiplier));
        BigDecimal lowerBand = middleBand.subtract(standardDeviation.multiply(multiplier));

        return new BollingerResult(
                upperBand.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                middleBand.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                lowerBand.setScale(RESULT_SCALE, RoundingMode.HALF_UP)
        );
    }

    /**
     * 提取收盘价列表
     */
    private static List<BigDecimal> extractClosePrices(List<KLine> kLineList) {
        List<BigDecimal> closePrices = new ArrayList<>();
        for (KLine kLine : kLineList) {
            closePrices.add(kLine.getClosePrice());
        }
        return closePrices;
    }

    /**
     * 计算简单移动平均(SMA)
     */
    private static BigDecimal calculateSma(List<BigDecimal> prices, int period) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal price : prices) {
            sum = sum.add(price);
        }
        return sum.divide(BigDecimal.valueOf(period), CALCULATION_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 计算样本标准差（金融分析标准）
     */
    private static BigDecimal calculateSampleStandardDeviation(List<BigDecimal> prices, BigDecimal mean, int period) {
        BigDecimal sumSquaredDeviations = BigDecimal.ZERO;

        for (BigDecimal price : prices) {
            BigDecimal deviation = price.subtract(mean);
            BigDecimal squaredDeviation = deviation.multiply(deviation);
            sumSquaredDeviations = sumSquaredDeviations.add(squaredDeviation);
        }

        // 使用样本标准差：除以 (n-1) 而不是 n
        if (period > 1) {
            BigDecimal variance = sumSquaredDeviations.divide(
                    BigDecimal.valueOf(period - 1), CALCULATION_SCALE, RoundingMode.HALF_UP);
            return sqrt(variance);
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 计算平方根（带缓存的优化版本）
     */
    private static BigDecimal sqrt(BigDecimal number) {
        if (number.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        if (number.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        // 使用更高效的初始猜测值
        BigDecimal initialGuess = BigDecimal.valueOf(Math.sqrt(number.doubleValue()));
        if (initialGuess.compareTo(BigDecimal.ZERO) == 0) {
            initialGuess = number.divide(BigDecimal.valueOf(2), CALCULATION_SCALE, RoundingMode.HALF_UP);
        }

        BigDecimal x = initialGuess;
        BigDecimal precision = BigDecimal.ONE.scaleByPowerOfTen(-CALCULATION_SCALE);
        int maxIterations = 20;

        for (int i = 0; i < maxIterations; i++) {
            BigDecimal xNew = x.add(number.divide(x, CALCULATION_SCALE, RoundingMode.HALF_UP))
                    .divide(BigDecimal.valueOf(2), CALCULATION_SCALE, RoundingMode.HALF_UP);

            if (x.subtract(xNew).abs().compareTo(precision) < 0) {
                return xNew;
            }
            x = xNew;
        }

        return x;
    }

    /**
     * 创建空的布林带结果
     */
    private static BollingerResult createEmptyResult() {
        return new BollingerResult(
                BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP)
        );
    }

    /**
     * 检查数据是否足够计算布林带
     */
    public static boolean isDataSufficient(List<KLine> kLineList, int period) {
        return kLineList != null && kLineList.size() >= period;
    }

    public static boolean isDataSufficient(List<KLine> kLineList) {
        return isDataSufficient(kLineList, DEFAULT_PERIOD);
    }
}