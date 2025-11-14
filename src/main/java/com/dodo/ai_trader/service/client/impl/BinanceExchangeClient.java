package com.dodo.ai_trader.service.client.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.binance.connector.client.common.ApiResponse;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.api.DerivativesTradingUsdsFuturesRestApi;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.model.*;
import com.dodo.ai_trader.service.client.ExchangeClient;
import com.dodo.ai_trader.service.enums.ExchangeIntervalEnum;
import com.dodo.ai_trader.service.enums.PositionOrderStatus;
import com.dodo.ai_trader.service.enums.SideEnum;
import com.dodo.ai_trader.service.model.OpenPositionOrder;
import com.dodo.ai_trader.service.model.Signal;
import com.dodo.ai_trader.service.model.market.ExchangeBalance;
import com.dodo.ai_trader.service.model.market.ExchangePosition;
import com.dodo.ai_trader.service.model.market.FundingRate;
import com.dodo.ai_trader.service.model.market.KLine;
import com.dodo.ai_trader.service.repository.OpenPositionOrderRepository;
import com.dodo.ai_trader.service.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("binance")
public class BinanceExchangeClient implements ExchangeClient {

    @Autowired
    private DerivativesTradingUsdsFuturesRestApi binanceFuturesRestApi;

    @Autowired
    private OpenPositionOrderRepository openPositionOrderRepository;

    @Override
    public BigDecimal getCurrentPrice(String symbol) {
        ApiResponse<SymbolPriceTickerV2Response> priceTickerV2 = binanceFuturesRestApi.symbolPriceTickerV2(convertCommonPair(symbol));
        JSONObject data = JSON.parseObject(priceTickerV2.getData().toJson());
        return new BigDecimal(data.getString("price"));
    }

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
    public BigDecimal getAvgOpenInterest(String symbol) {
        ApiResponse<OpenInterestStatisticsResponse> apiResponse = binanceFuturesRestApi.openInterestStatistics(convertCommonPair(symbol), Period.PERIOD_1h, 24L, null, null);
        if (apiResponse != null && apiResponse.getData() != null) {
            JSONArray objects = JSON.parseArray(apiResponse.getData().toJson());
            if (objects == null || objects.isEmpty()) {
                return null;
            }
            BigDecimal total = BigDecimal.ZERO;
            for (int i = 0; i < objects.size(); i++) {
                JSONObject jsonObject = objects.getJSONObject(i);
                total = total.add(new BigDecimal(jsonObject.getString("sumOpenInterest")));
            }
            return total.divide(new BigDecimal(objects.size()), 3, BigDecimal.ROUND_HALF_UP);
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

    @Override
    public ExchangeBalance getBalance() {
        ApiResponse<AccountInformationV3Response> informationV3 = binanceFuturesRestApi.accountInformationV3(5000L);
        JSONObject data = JSON.parseObject(informationV3.getData().toJson());
        ExchangeBalance exchangeBalance = new ExchangeBalance();
        exchangeBalance.setTotalWalletBalance(new BigDecimal(data.getString("totalWalletBalance")));
        exchangeBalance.setTotalUnrealizedProfit(new BigDecimal(data.getString("totalUnrealizedProfit")));
        exchangeBalance.setAvailableBalance(new BigDecimal(data.getString("availableBalance")));
        return exchangeBalance;
    }

    @Override
    public List<ExchangePosition> getPosition(String symbol) {
        ApiResponse<PositionInformationV2Response> positionInformationV2 = binanceFuturesRestApi.positionInformationV2(convertCommonPair(symbol), null);
        JSONArray objects = JSON.parseArray(positionInformationV2.getData().toJson());
        if (objects == null || objects.isEmpty()) {
            return null;
        }
        List<ExchangePosition> list = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            JSONObject jsonObject = objects.getJSONObject(i);
            BigDecimal positionAmt = new BigDecimal(jsonObject.getString("positionAmt"));
            if (positionAmt.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            ExchangePosition exchangePosition = new ExchangePosition();
            exchangePosition.setSymbol(jsonObject.getString("symbol"));
            exchangePosition.setSide(positionAmt.compareTo(BigDecimal.ZERO) > 0 ? SideEnum.LONG : SideEnum.SHORT);
            exchangePosition.setPositionAmt(positionAmt);
            exchangePosition.setEntryPrice(new BigDecimal(jsonObject.getString("entryPrice")));
            exchangePosition.setMarkPrice(new BigDecimal(jsonObject.getString("markPrice")));
            exchangePosition.setUnRealizedProfit(new BigDecimal(jsonObject.getString("unRealizedProfit")));
            exchangePosition.setLeverage(Integer.parseInt(jsonObject.getString("leverage")));
            exchangePosition.setLiquidationPrice(new BigDecimal(jsonObject.getString("liquidationPrice")));
            list.add(exchangePosition);
        }
        return list.size() == 0 ? null : list;
    }

    @Override
    public void openLongPosition(String bizId, String userId, Signal signal) {

        OpenPositionOrder old = openPositionOrderRepository.findByClientOrderId(bizId, "binance");
        if (old != null) {
            return;
        }

        changeMarginType(signal.getCoin());
        BigDecimal currentPrice = getCurrentPrice(signal.getCoin());
        if (signal.getEntryPrice().compareTo(currentPrice) > 0) {
            signal.setEntryPrice(currentPrice.subtract(getDiffPrice(signal.getCoin())));
        }

        NewOrderRequest request = new NewOrderRequest();
        request.setSymbol(convertCommonPair(signal.getCoin()));
        request.setSide(Side.BUY);
        request.setType("LIMIT");
        request.setTimeInForce(TimeInForce.GTC);
        request.setQuantity(signal.getQuantity().doubleValue());
        request.setPrice(signal.getEntryPrice().doubleValue());
        request.setNewClientOrderId(bizId);
        ApiResponse<NewOrderResponse> newOrder = binanceFuturesRestApi.newOrder(request);
        Long orderId = newOrder.getData().getOrderId();
        LogUtil.serviceLog("开多仓, bizId: {}, orderId: {}, signal: {}", bizId, orderId, signal);
        savePositionOrder(bizId, orderId, userId, SideEnum.LONG, signal);
    }

    private void savePositionOrder(String bizId, Long orderId, String userId, SideEnum side, Signal signal) {
        OpenPositionOrder openPositionOrder = new OpenPositionOrder();
        openPositionOrder.setClientOrderId(bizId);
        openPositionOrder.setOrderId(orderId.toString());
        openPositionOrder.setUserId(userId);
        openPositionOrder.setExchange("binance");
        openPositionOrder.setSymbol(signal.getCoin());
        openPositionOrder.setSide(side);
        openPositionOrder.setType("LIMIT");
        openPositionOrder.setTimeInForce("GTC");
        openPositionOrder.setQuantity(signal.getQuantity());
        openPositionOrder.setEntryPrice(signal.getEntryPrice());
        openPositionOrder.setStopPrice(signal.getStopLoss());
        openPositionOrder.setProfitTarget(signal.getProfitTarget());
        openPositionOrder.setOrderTime(new Date());
        openPositionOrder.setStatus(PositionOrderStatus.PENDING);
        openPositionOrder.setVersion(1);
        openPositionOrderRepository.save(openPositionOrder);

    }

    private BigDecimal getDiffPrice(String symbol) {
        if (symbol.equals("BTC")) {
            return new BigDecimal("30");
        }
        if (symbol.equals("ETH")) {
            return new BigDecimal("10");
        }
        if (symbol.equals("BNB")) {
            return new BigDecimal("5");
        }
        return new BigDecimal("1");
    }

    private void changeMarginType(String symbol) {
        ChangeMarginTypeRequest marginTypeRequest = new ChangeMarginTypeRequest();
        marginTypeRequest.setMarginType(MarginType.ISOLATED);
        marginTypeRequest.setSymbol(convertCommonPair(symbol));
        ApiResponse<ChangeMarginTypeResponse> response = binanceFuturesRestApi.changeMarginType(marginTypeRequest);
        if (response != null && response.getData().getCode() == 200) {
            LogUtil.serviceLog("修改保证金成功, symbol=" + symbol);
        } else {
            LogUtil.serviceLog("修改保证金失败, symbol=" + symbol);
        }
    }

    @Override
    public void openShortPosition(String bizId, String userId, Signal signal) {
        OpenPositionOrder old = openPositionOrderRepository.findByClientOrderId(bizId, "binance");
        if (old != null) {
            return;
        }
        changeMarginType(signal.getCoin());
        BigDecimal currentPrice = getCurrentPrice(signal.getCoin());
        if (signal.getEntryPrice().compareTo(currentPrice) < 0) {
            signal.setEntryPrice(currentPrice.add(getDiffPrice(signal.getCoin())));
        }

        NewOrderRequest request = new NewOrderRequest();
        request.setSymbol(convertCommonPair(signal.getCoin()));
        request.setSide(Side.SELL);
        request.setType("LIMIT");
        request.setTimeInForce(TimeInForce.GTC);
        request.setQuantity(signal.getQuantity().doubleValue());
        request.setPrice(signal.getEntryPrice().doubleValue());
        request.setNewClientOrderId(bizId);
        ApiResponse<NewOrderResponse> newOrder = binanceFuturesRestApi.newOrder(request);
        Long orderId = newOrder.getData().getOrderId();
        LogUtil.serviceLog("开多仓, bizId: {}, orderId: {}, signal: {}", bizId, orderId, signal);
        savePositionOrder(bizId, orderId, userId, SideEnum.SHORT, signal);
    }

    @Override
    public PositionOrderStatus getPositionOrderStatus(OpenPositionOrder openPositionOrder) {
        ApiResponse<QueryOrderResponse> response = binanceFuturesRestApi.queryOrder(convertCommonPair(openPositionOrder.getSymbol()), Long.parseLong(openPositionOrder.getOrderId()), openPositionOrder.getClientOrderId(), null);
        if (response == null || response.getData() == null) {
            return null;
        }
        LogUtil.serviceLog("查询订单状态, orderId: {}, response: {}", openPositionOrder.getOrderId(), response.getData().toJson());
        return convertStatus(response.getData().getStatus());
    }

    // TODO 需要测试一下修改映射
    private PositionOrderStatus convertStatus(String status) {
        if (status.equals("NEW")) {
            return PositionOrderStatus.PENDING;
        }
        if (status.equals("FILLED")) {
            return PositionOrderStatus.FILLED;
        }
        if (status.equals("CANCELED")) {
            return PositionOrderStatus.CANCELED;
        }
        if (status.equals("EXPIRED")) {
            return PositionOrderStatus.EXPIRED;
        }
        if (status.equals("COMPLETED")) {
            return PositionOrderStatus.COMPLETED;
        }
        return null;
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
