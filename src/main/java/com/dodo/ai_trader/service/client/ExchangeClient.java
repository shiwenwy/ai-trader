package com.dodo.ai_trader.service.client;

import com.dodo.ai_trader.service.enums.ExchangeIntervalEnum;
import com.dodo.ai_trader.service.model.KLine;

import java.util.List;

public interface ExchangeClient {

    List<KLine> getFuturesKLine(String symbol, ExchangeIntervalEnum interval, Integer limit);
}
