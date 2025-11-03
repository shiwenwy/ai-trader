package com.dodo.ai_trader.service.Indicator;

import com.dodo.ai_trader.service.model.market.KLine;
import com.dodo.ai_trader.service.model.market.MacdResult;
import com.dodo.ai_trader.service.utils.LogUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MacdCalculator {

    // MACD默认参数
    private static final int DEFAULT_FAST_PERIOD = 12;
    private static final int DEFAULT_SLOW_PERIOD = 26;
    private static final int DEFAULT_SIGNAL_PERIOD = 9;

    // 结果精度
    private static final int RESULT_SCALE = 8;

    /**
     * 计算单个MACD值
     * @param kLineList K线数据列表
     * @return 最新的MACD结果
     */
    public static MacdResult calculateMacd(List<KLine> kLineList) {
        return calculateMacd(kLineList, DEFAULT_FAST_PERIOD, DEFAULT_SLOW_PERIOD, DEFAULT_SIGNAL_PERIOD);
    }

    /**
     * 计算指定参数的MACD值
     * @param kLineList K线数据列表
     * @param fastPeriod 快速EMA周期 (通常为12)
     * @param slowPeriod 慢速EMA周期 (通常为26)
     * @param signalPeriod 信号线周期 (通常为9)
     * @return 最新的MACD结果
     */
    public static MacdResult calculateMacd(List<KLine> kLineList, int fastPeriod, int slowPeriod, int signalPeriod) {
        // 数据充分性检查
        int minDataSize = Math.max(slowPeriod, fastPeriod) + signalPeriod;
        if (kLineList == null || kLineList.size() < minDataSize) {
            return new MacdResult(
                    BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                    BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                    BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP)
            );
        }

        try {
            // 计算快速EMA和慢速EMA
            List<BigDecimal> fastEmaList = EmaCalculator.calculateEmaList(kLineList, fastPeriod);
            List<BigDecimal> slowEmaList = EmaCalculator.calculateEmaList(kLineList, slowPeriod);

            // 确保EMA列表长度一致
            int startIndex = Math.max(fastEmaList.size() - slowEmaList.size(), 0);
            int minSize = Math.min(fastEmaList.size() - startIndex, slowEmaList.size());

            // 计算MACD线 (快EMA - 慢EMA)
            List<BigDecimal> macdLineList = new ArrayList<>();
            for (int i = 0; i < minSize; i++) {
                BigDecimal fastEma = fastEmaList.get(startIndex + i);
                BigDecimal slowEma = slowEmaList.get(i);
                BigDecimal macdLine = fastEma.subtract(slowEma);
                macdLineList.add(macdLine);
            }

            // 计算信号线 (MACD的EMA)
            List<BigDecimal> signalLineList = EmaCalculator.calculateEmaListWithAmount(macdLineList, signalPeriod);

            if (signalLineList.isEmpty()) {
                return new MacdResult(
                        BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                        BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                        BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP)
                );
            }

            // 获取最新的值
            BigDecimal latestMacdLine = macdLineList.get(macdLineList.size() - 1)
                    .setScale(RESULT_SCALE, RoundingMode.HALF_UP);
            BigDecimal latestSignalLine = signalLineList.get(signalLineList.size() - 1)
                    .setScale(RESULT_SCALE, RoundingMode.HALF_UP);
            BigDecimal histogram = latestMacdLine.subtract(latestSignalLine)
                    .setScale(RESULT_SCALE, RoundingMode.HALF_UP);

            return new MacdResult(latestMacdLine, latestSignalLine, histogram);

        } catch (Exception e) {
            // 记录日志
            LogUtil.error("MACD计算错误", e);
            return new MacdResult(
                    BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                    BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                    BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP)
            );
        }
    }

    /**
     * 计算多个MACD值，返回每个K线对应的MACD结果列表
     * @param kLineList K线数据列表
     * @return MACD结果列表
     */
    public static List<MacdResult> calculateMacdList(List<KLine> kLineList) {
        return calculateMacdList(kLineList, DEFAULT_FAST_PERIOD, DEFAULT_SLOW_PERIOD, DEFAULT_SIGNAL_PERIOD);
    }

    /**
     * 计算指定参数的多个MACD值
     * @param kLineList K线数据列表
     * @param fastPeriod 快速EMA周期
     * @param slowPeriod 慢速EMA周期
     * @param signalPeriod 信号线周期
     * @return MACD结果列表
     */
    public static List<MacdResult> calculateMacdList(List<KLine> kLineList, int fastPeriod, int slowPeriod, int signalPeriod) {
        List<MacdResult> macdResults = new ArrayList<>();

        // 数据充分性检查
        int minDataSize = Math.max(slowPeriod, fastPeriod) + signalPeriod;
        if (kLineList == null || kLineList.size() < minDataSize) {
            return macdResults;
        }

        try {
            // 计算快速EMA和慢速EMA
            List<BigDecimal> fastEmaList = EmaCalculator.calculateEmaList(kLineList, fastPeriod);
            List<BigDecimal> slowEmaList = EmaCalculator.calculateEmaList(kLineList, slowPeriod);

            // 对齐EMA列表（从较长的EMA开始）
            int startIndex = Math.max(fastEmaList.size() - slowEmaList.size(), 0);
            int minSize = Math.min(fastEmaList.size() - startIndex, slowEmaList.size());

            // 计算MACD线 (快EMA - 慢EMA)
            List<BigDecimal> macdLineList = new ArrayList<>();
            for (int i = 0; i < minSize; i++) {
                BigDecimal fastEma = fastEmaList.get(startIndex + i);
                BigDecimal slowEma = slowEmaList.get(i);
                BigDecimal macdLine = fastEma.subtract(slowEma);
                macdLineList.add(macdLine);
            }

            // 计算信号线 (MACD的EMA)
            List<BigDecimal> signalLineList = EmaCalculator.calculateEmaListWithAmount(macdLineList, signalPeriod);

            // 构建结果列表（只返回有信号线的数据）
            int resultStartIndex = macdLineList.size() - signalLineList.size();
            for (int i = 0; i < signalLineList.size(); i++) {
                int macdIndex = resultStartIndex + i;
                if (macdIndex >= 0 && macdIndex < macdLineList.size()) {
                    BigDecimal macdLine = macdLineList.get(macdIndex).setScale(RESULT_SCALE, RoundingMode.HALF_UP);
                    BigDecimal signalLine = signalLineList.get(i).setScale(RESULT_SCALE, RoundingMode.HALF_UP);
                    BigDecimal histogram = macdLine.subtract(signalLine).setScale(RESULT_SCALE, RoundingMode.HALF_UP);
                    macdResults.add(new MacdResult(macdLine, signalLine, histogram));
                }
            }

        } catch (Exception e) {
            // 记录日志
            LogUtil.error("MACD列表计算错误", e);
        }

        return macdResults;
    }

    /**
     * 检查数据是否足够计算MACD
     * @param kLineList K线数据列表
     * @param fastPeriod 快速周期
     * @param slowPeriod 慢速周期
     * @param signalPeriod 信号周期
     * @return 是否足够
     */
    public static boolean isDataSufficient(List<KLine> kLineList, int fastPeriod, int slowPeriod, int signalPeriod) {
        if (kLineList == null) return false;
        int minDataSize = Math.max(slowPeriod, fastPeriod) + signalPeriod;
        return kLineList.size() >= minDataSize;
    }

    public static boolean isDataSufficient(List<KLine> kLineList) {
        return isDataSufficient(kLineList, DEFAULT_FAST_PERIOD, DEFAULT_SLOW_PERIOD, DEFAULT_SIGNAL_PERIOD);
    }
}