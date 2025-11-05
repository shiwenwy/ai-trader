package com.dodo.ai_trader.service.Indicator;

import java.util.ArrayList;
import java.util.List;

public class SharpeRatioCalculator {

    /**
     * 计算夏普比率
     *
     * @param totalEquity 账户净值列表
     * @return 夏普比率
     */
    public static double calculateSharpeRatio(List<Double> totalEquity) {
        if (totalEquity == null || totalEquity.isEmpty()) {
            return 0.0;
        }
        if (totalEquity.size() < 2) {
            return 0.0;
        }

        // 计算周期收益率（period returns）
        List<Double> returns = new ArrayList<>();
        for (int i = 1; i < totalEquity.size(); i++) {
            double prevEquity = totalEquity.get(i - 1);
            if (prevEquity > 0) {
                double periodReturn = (totalEquity.get(i) - prevEquity) / prevEquity;
                returns.add(periodReturn);
            }
        }

        if (returns.isEmpty()) {
            return 0.0;
        }

        // 计算平均收益率
        double sumReturns = 0.0;
        for (double r : returns) {
            sumReturns += r;
        }
        double meanReturn = sumReturns / returns.size();

        // 计算收益率标准差
        double sumSquaredDiff = 0.0;
        for (double r : returns) {
            double diff = r - meanReturn;
            sumSquaredDiff += diff * diff;
        }
        double variance = sumSquaredDiff / returns.size();
        double stdDev = Math.sqrt(variance);

        // 避免除以零
        if (stdDev == 0) {
            if (meanReturn > 0) {
                return 999.0; // 无波动的正收益
            } else if (meanReturn < 0) {
                return -999.0; // 无波动的负收益
            }
            return 0.0;
        }

        // 计算夏普比率（假设无风险利率为0）
        // 注：直接返回周期级别的夏普比率（非年化），正常范围 -2 到 +2
        double sharpeRatio = meanReturn / stdDev;
        return sharpeRatio;
    }

    public static void main(String[] args) {
        List<Double> totalEquity = new ArrayList<>();
        totalEquity.add(1000.0);
        totalEquity.add(1001.0);
        totalEquity.add(999.0);
        totalEquity.add(999.0);

        double sharpeRatio = calculateSharpeRatio(totalEquity);
        System.out.println("夏普比率：" + sharpeRatio);
    }
}
