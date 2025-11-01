package com.dodo.ai_trader.service.client.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.binance.connector.client.common.ApiResponse;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.api.DerivativesTradingUsdsFuturesRestApi;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.model.Interval;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.model.KlineCandlestickDataResponse;
import com.dodo.ai_trader.service.client.ExchangeClient;
import com.dodo.ai_trader.service.enums.ExchangeIntervalEnum;
import com.dodo.ai_trader.service.model.KLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component("binance")
public class BinanceExchangeClient implements ExchangeClient {

    @Autowired
    private DerivativesTradingUsdsFuturesRestApi binanceFuturesRestApi;

    @Override
    public List<KLine> getFuturesKLine(String symbol, ExchangeIntervalEnum interval, Integer limit) {
        ApiResponse<KlineCandlestickDataResponse> btcusdt = binanceFuturesRestApi.klineCandlestickData(symbol.toUpperCase() + "USDT", convertInterval(interval), null, null, limit.longValue());
        JSONArray objects = JSON.parseArray(btcusdt.getData().toJson());
        if (objects == null || objects.isEmpty()) {
            return null;
        }
        List<KLine> list = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            JSONArray jsonArray = objects.getJSONArray(i);
            KLine kLine = new KLine();
            kLine.setOpenTime(Long.parseLong(jsonArray.getString(0)));
            kLine.setCloseTime(Long.parseLong(jsonArray.getString(6)));
            kLine.setOpenPrice(new BigDecimal(jsonArray.getString(1)));
            kLine.setHighPrice(new BigDecimal(jsonArray.getString(2)));
            kLine.setLowPrice(new BigDecimal(jsonArray.getString(3)));
            kLine.setClosePrice(new BigDecimal(jsonArray.getString(4)));
            kLine.setVolume(new BigDecimal(jsonArray.getString(5)));
            kLine.setQuoteAssetVolume(new BigDecimal(jsonArray.getString(7)));
            kLine.setTradeNum(Integer.parseInt(jsonArray.getString(8)));
            kLine.setTakerBuyBaseVolume(new BigDecimal(jsonArray.getString(9)));
            kLine.setTakerBuyQuoteVolume(new BigDecimal(jsonArray.getString(10)));
            list.add(kLine);
        }
        return list;
    }


    private Interval convertInterval(ExchangeIntervalEnum interval) {
        return Interval.fromValue(interval.getValue());
    }
}
