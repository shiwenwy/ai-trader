package com.dodo.ai_trader.service.client.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.binance.connector.client.common.ApiResponse;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.api.DerivativesTradingUsdsFuturesRestApi;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.model.GetFundingRateHistoryResponse;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.model.Interval;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.model.KlineCandlestickDataResponse;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.model.OpenInterestResponse;
import com.dodo.ai_trader.service.client.ExchangeClient;
import com.dodo.ai_trader.service.enums.ExchangeIntervalEnum;
import com.dodo.ai_trader.service.model.market.FundingRate;
import com.dodo.ai_trader.service.model.market.KLine;
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
        ApiResponse<KlineCandlestickDataResponse> klines = binanceFuturesRestApi.klineCandlestickData(convertCommonPair(symbol), convertInterval(interval), null, null, limit.longValue());
        JSONArray objects = JSON.parseArray(klines.getData().toJson());
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

    @Override
    public BigDecimal getOpenInterest(String symbol) {
        ApiResponse<OpenInterestResponse> openInterest = binanceFuturesRestApi.openInterest(convertCommonPair(symbol));
        if (openInterest != null && openInterest.getData() != null) {
            return new BigDecimal(openInterest.getData().getOpenInterest());
        }
        return null;
    }

    @Override
    public List<FundingRate> getLastFundingRate(String symbol) {
        ApiResponse<GetFundingRateHistoryResponse> fundingRateHistory = binanceFuturesRestApi.getFundingRateHistory(convertCommonPair(symbol), null, null, 5L);

        JSONArray objects = JSON.parseArray(fundingRateHistory.getData().toJson());
        if (objects == null || objects.isEmpty()) {
            return null;
        }
        List<FundingRate> list = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            JSONObject jsonObject = objects.getJSONObject(i);
            FundingRate fundingRate = new FundingRate();
            fundingRate.setFundingRate(new BigDecimal(jsonObject.getString("fundingRate")));
            fundingRate.setFundingTime(jsonObject.getLong("fundingTime"));
            fundingRate.setMarkPrice(new BigDecimal(jsonObject.getString("markPrice")));
            list.add(fundingRate);
        }
        return list;
    }


    private Interval convertInterval(ExchangeIntervalEnum interval) {
        return Interval.fromValue(interval.getValue());
    }

    private String convertCommonPair(String symbol) {
        String upperCase = symbol.toUpperCase();
        if (upperCase.contains("USDT")) {
            return upperCase;
        }
        return upperCase + "USDT";
    }
}
