package com.dodo.ai_trader.biz.task;

import com.dodo.ai_trader.service.client.ExchangeClient;
import com.dodo.ai_trader.service.constant.SystemConstant;
import com.dodo.ai_trader.service.model.market.ExchangePosition;
import com.dodo.ai_trader.service.service.RiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class AutoRiskTask {

    @Autowired
    private Map<String, ExchangeClient> exchangeClientMap;
    @Autowired
    private RiskService riskService;

    private static final List<String> SYMBOL_LIST = List.of("BTC", "ETH", "BNB", "SOL");

    @Scheduled(cron = "0/10 * * * * ?")
    public void autoBinanceRiskControl() {
        ExchangeClient binance = exchangeClientMap.get("binance");

        for (String symbol : SYMBOL_LIST) {
            List<ExchangePosition> positionList = binance.getPosition(symbol);
            if (positionList == null || positionList.isEmpty()) {
                continue;
            }
            for (ExchangePosition position : positionList) {
                riskService.riskCheckAfterTrade(SystemConstant.DEFAULT_USER_ID, "binance", position);
            }
        }
    }
}
