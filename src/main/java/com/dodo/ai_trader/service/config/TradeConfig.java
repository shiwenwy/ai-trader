package com.dodo.ai_trader.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "trade.config")
@Data
public class TradeConfig {

    private Integer altcoinMin;

    private Integer altcoinMax;

    private Integer altcoinLeverage;

    private Integer btcMin;

    private Integer btcMax;

    private Integer btcLeverage;
}
