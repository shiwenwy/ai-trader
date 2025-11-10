package com.dodo.ai_trader;

import com.binance.connector.client.common.ApiResponse;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.api.DerivativesTradingUsdsFuturesRestApi;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.model.FuturesAccountBalanceV3Response;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.model.NewOrderRequest;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.model.NewOrderResponse;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.model.Side;
import com.dodo.ai_trader.service.constant.SystemConstant;
import com.dodo.ai_trader.service.model.DecisionResult;
import com.dodo.ai_trader.service.repository.DecisionResultRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TradeTest {

    @Test
    void contextLoads() {
    }

    @Autowired
    private DecisionResultRepository decisionResultRepository;

    @Test
    public void test() {
        List<DecisionResult> resultList = decisionResultRepository.getLastDecisionResultList(SystemConstant.DEFAULT_USER_ID, "binance", 10);
        System.out.println(resultList);
    }

    @Autowired
    private DerivativesTradingUsdsFuturesRestApi binanceFuturesRestApi;

    @Test
    public void test1() {
        ApiResponse<FuturesAccountBalanceV3Response> balanceV3 = binanceFuturesRestApi.futuresAccountBalanceV3(null);
        System.out.println(balanceV3.getData().toJson());
    }

    @Test
    public void test2() {
        NewOrderRequest request = new NewOrderRequest();
        request.symbol("BTCUSDT")
                .side(Side.SELL)
                .type("MARKET")
                .quantity(0.01);
        ApiResponse<NewOrderResponse> newOrder = binanceFuturesRestApi.newOrder(request);
        System.out.println(newOrder.getData().toJson());
    }
}
