package com.dodo.ai_trader;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.binance.connector.client.common.ApiResponse;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.api.DerivativesTradingUsdsFuturesRestApi;
import com.binance.connector.client.derivatives_trading_usds_futures.rest.model.*;
import com.dodo.ai_trader.service.Indicator.MiddlePriceCalculator;
import com.dodo.ai_trader.service.client.ExchangeClient;
import com.dodo.ai_trader.service.decision.BinanceDecision;
import com.dodo.ai_trader.service.enums.ExchangeIntervalEnum;
import com.dodo.ai_trader.service.model.CommonPosition;
import com.dodo.ai_trader.service.model.market.KLine;
import com.dodo.ai_trader.service.utils.AiResParseUtil;
import com.dodo.ai_trader.service.utils.LogUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class AiTraderApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private BinanceDecision binanceDecision;

	@Test
	public void test() {
		List<CommonPosition> positions = new ArrayList<>();
		CommonPosition btc = new CommonPosition();
		btc.setSymbol("BTC");
		btc.setSide("LONG");
		btc.setQuantity(0.5);
		btc.setEntryPrice(30000.0);
		btc.setCurrentPrice(30500.0);
		btc.setLeverage(20);
		btc.setProfitTarget(0.05);
		btc.setStopLoss(0.03);
		btc.setInvalidationCondition("btc_price > 31000");
		btc.setConfidence(0.8);
		btc.setRiskUsd(5000.0);
		positions.add(btc);
//		CommonPosition eth = new CommonPosition();
//		eth.setSymbol("ETH");
//		eth.setSide("SHORT");
//		eth.setQuantity(0.5);
//		eth.setEntryPrice(2000.0);
//		eth.setCurrentPrice(2050.0);
//		eth.setLeverage(20);
//		eth.setProfitTarget(0.05);
//		eth.setStopLoss(0.02);
//		eth.setInvalidationCondition("eth_price > 2100");
//		eth.setConfidence(0.8);
//		positions.add(eth);

		// 准备BTC数据
		Map<String, Object> btcData = new HashMap<>();
		btcData.put("btc_price", 30500.0);
		btcData.put("btc_ema20", 30200.0);
		btcData.put("btc_macd", 150.0);
		btcData.put("btc_rsi7", 65.5);
		btcData.put("btc_oi_latest", 1200000);
		btcData.put("btc_oi_avg", 1100000);
		btcData.put("btc_funding_rate", 0.0001);
		btcData.put("btc_prices_5m", "29800,29900,30000,30100,30200,30300,30400,30500");
		btcData.put("btc_ema20_5m", "29700,29800,29900,30000,30100,30200,30300,30400");
		btcData.put("btc_macd_5m", "100,110,120,130,140,150,160,170");
		btcData.put("btc_rsi7_5m", "55,58,60,62,63,64,65,65.5");
		btcData.put("btc_rsi14_5m", "50,52,54,56,58,59,60,61");
		btcData.put("btc_ema20_4h", 29500.0);
		btcData.put("btc_ema50_4h", 29000.0);
		btcData.put("btc_atr3_4h", 800.0);
		btcData.put("btc_atr14_4h", 750.0);
		btcData.put("btc_volume_current", 1500000000L);
		btcData.put("btc_volume_avg", 1200000000L);
		btcData.put("btc_macd_4h", "200,220,240,260");
		btcData.put("btc_rsi14_4h", "58,60,62,64");

		// 准备ETH数据
		Map<String, Object> ethData = new HashMap<>();
		ethData.put("eth_price", 2050.0);
		ethData.put("eth_ema20", 2020.0);
		ethData.put("eth_macd", 25.0);
		ethData.put("eth_rsi7", 58.2);
		ethData.put("eth_oi_latest", 800000);
		ethData.put("eth_oi_avg", 750000);
		ethData.put("eth_funding_rate", 0.0002);
		ethData.put("eth_prices_5m", "1980,1990,2000,2010,2020,2030,2040,2050");
		ethData.put("eth_ema20_5m", "1970,1980,1990,2000,2010,2020,2030,2040");
		ethData.put("eth_macd_5m", "15,17,19,21,23,24,25,26");
		ethData.put("eth_rsi7_5m", "50,52,54,55,56,57,58,58.2");
		ethData.put("eth_rsi14_5m", "48,49,50,52,54,55,56,57");
		ethData.put("eth_ema20_4h", 1950.0);
		ethData.put("eth_ema50_4h", 1900.0);
		ethData.put("eth_atr3_4h", 60.0);
		ethData.put("eth_atr14_4h", 55.0);
		ethData.put("eth_volume_current", 800000000L);
		ethData.put("eth_volume_avg", 700000000L);
		ethData.put("eth_macd_4h", "20,22,24,25");
		ethData.put("eth_rsi14_4h", "55,56,57,58");

		Map<String, Object> solData = new HashMap<>();
		solData.put("sol_price", 20.0);
		solData.put("sol_ema20", 18.0);
		solData.put("sol_macd", 5.0);
		solData.put("sol_rsi7", 50.0);
		solData.put("sol_oi_latest", 500000);
		solData.put("sol_oi_avg", 450000);
		solData.put("sol_funding_rate", 0.00005);
		solData.put("sol_prices_5m", "15,16,17,18,19,20,21,22");
		solData.put("sol_ema20_5m", "14,15,16,17,18,19,20,21");
		solData.put("sol_macd_5m", "5,7,9,11,13,14,15,16");
		solData.put("sol_rsi7_5m", "40,42,44,46,48,50,52,54");
		solData.put("sol_rsi14_5m", "35,37,39,41,43,45,47,49");
		solData.put("sol_ema20_4h", 15.0);
		solData.put("sol_ema50_4h", 15.0);
		solData.put("sol_atr3_4h", 5.0);
		solData.put("sol_atr14_4h", 5.0);
		solData.put("sol_volume_current", 500000000L);
		solData.put("sol_volume_avg", 450000000L);
		solData.put("sol_macd_4h", "5,7,9,11,13,14,15,16");
		solData.put("sol_rsi14_4h", "40,42,44,46,48,50,52,54");

		Map<String, Object> bnbData = new HashMap<>();
		bnbData.put("bnb_price", 30.0);
		bnbData.put("bnb_ema20", 28.0);
		bnbData.put("bnb_macd", 5.0);
		bnbData.put("bnb_rsi7", 50.0);
		bnbData.put("bnb_oi_latest", 500000);
		bnbData.put("bnb_oi_avg", 450000);
		bnbData.put("bnb_funding_rate", 0.00005);
		bnbData.put("bnb_prices_5m", "25,26,27,28,29,30,31,32");
		bnbData.put("bnb_ema20_5m", "24,25,26,27,28,29,30,31");
		bnbData.put("bnb_macd_5m", "5,7,9,11,13,14,15,16");
		bnbData.put("bnb_rsi7_5m", "40,42,44,46,48,50,52,54");
		bnbData.put("bnb_rsi14_5m", "35,37,39,41,43,45,47,49");
		bnbData.put("bnb_ema20_4h", 25.0);
		bnbData.put("bnb_ema50_4h", 25.0);
		bnbData.put("bnb_atr3_4h", 5.0);
		bnbData.put("bnb_atr14_4h", 5.0);
		bnbData.put("bnb_volume_current", 500000000L);
		bnbData.put("bnb_volume_avg", 450000000L);
		bnbData.put("bnb_macd_4h", "5,7,9,11,13,14,15,16");
		bnbData.put("bnb_rsi14_4h", "40,42,44,46,48,50,52,54");

//		Message systemPrompt = binanceDecision.buildSystemMessage(10000);
//
//		Message userPrompt = binanceDecision.buildUserMessage(100, btcData, ethData, solData, bnbData, 0.0, 0.0, 1000000.0, 1000000.0, positions);
//
//		Prompt prompt = new Prompt(systemPrompt, userPrompt);
//		System.out.println(prompt.getContents());
//		System.out.println(systemPrompt.getText());
//		System.out.println(userPrompt.getText());

		Flux<String> decision = binanceDecision.getDecision(100, btcData, ethData, solData, bnbData, 0.0, 0.0, 1000000.0, 1000000.0, positions, 10000);

//		String result = decision.collect(StringBuilder::new, (sb, s) -> sb.append(s)).block().toString();
		String result = decision.collectList().block().stream().collect(Collectors.joining());


		System.out.println();
		LogUtil.serviceLog("返回成功");
		System.out.println("--------------------");
		System.out.println(result);
		System.out.println("--------------------");
		String res = AiResParseUtil.parseAiRes(result).trim();
		System.out.println(res);
		System.out.println("--------------------");
		JSONObject jsonObject = JSONUtil.parseObj(res);
		System.out.println(jsonObject.getStr("coin"));
		System.out.println(jsonObject.getStr("signal"));
		System.out.println(jsonObject.getFloat("profit_target"));
		System.out.println(jsonObject.getFloat("quantity"));
	}

	@Test
	public void test1() {
		String result = "**思维链分析：**\n" +
				"\n" +
				"当前持仓分析：BTC多头仓位表现良好，当前浮盈2.5%（30500 vs 30000入场），但尚未达到5%的盈利目标。趋势依然向上，EMA20持续支撑，MACD和RSI均显示健康上涨动量。\n" +
				"\n" +
				"新机会评估：\n" +
				"- BTC：强势上涨趋势，价格>EMA20，MACD持续上升，RSI 65.5未超买，资金费率微正，未平仓合约增加支撑趋势。但已持有仓位，不宜重复开仓。\n" +
				"- ETH：温和上涨，RSI 58.2中性偏强，但信号强度不如BTC明显。\n" +
				"- SOL/BNB：横盘震荡，无明显趋势信号，RSI中性，缺乏明确方向。\n" +
				"\n" +
				"风险考虑：当前夏普比率为0，需要谨慎开仓。BTC持仓已占用部分资金，且杠杆较高(20x)，应优先管理现有仓位而非开新仓。\n" +
				"\n" +
				"决策：维持BTC多头持仓，让利润继续奔跑。其他币种信号不足，观望为主。\n" +
				"\n" +
				"```json\n" +
				"{\n" +
				"  \"signal\": \"hold\",\n" +
				"  \"coin\": \"BTC\",\n" +
				"  \"quantity\": 0,\n" +
				"  \"leverage\": 1,\n" +
				"  \"profit_target\": 31500,\n" +
				"  \"stop_loss\": 29100,\n" +
				"  \"invalidation_condition\": \"BTC价格跌破29100或RSI7跌破50\",\n" +
				"  \"confidence\": 0.75,\n" +
				"  \"risk_usd\": 4500,\n" +
				"  \"justification\": \"BTC多头持仓表现良好，趋势持续向上。EMA20支撑有效，MACD和RSI显示健康上涨动量。未平仓合约增加支撑趋势。让利润奔跑至31500目标，同时设置29100止损保护收益。其他币种信号强度不足，优先管理现有高质量仓位。\"\n" +
				"}\n" +
				"```";
		String res = AiResParseUtil.parseAiRes(result).trim();
		System.out.println(res);

		JSONObject jsonObject = JSONUtil.parseObj(res);
		System.out.println(jsonObject.getStr("coin"));
		System.out.println(jsonObject.getStr("signal"));
		System.out.println(jsonObject.getFloat("profit_target"));
		System.out.println(jsonObject.getFloat("quantity"));
	}

	@Autowired
	private DerivativesTradingUsdsFuturesRestApi binanceFuturesRestApi;

	@Test
	public void test2() {
		ApiResponse<AccountInformationV3Response> informationV3 = binanceFuturesRestApi.accountInformationV3(5000L);
		System.out.println(informationV3.getData().toJson());
	}

	@Test
	public void test3() {
		ApiResponse<GetFundingRateHistoryResponse> btcusdt = binanceFuturesRestApi.getFundingRateHistory("BTCUSDT", null, null, null);
		System.out.println(btcusdt.getData().toJson());
	}

	@Test
	public void test4() {
		ApiResponse<PositionInformationV3Response> btcusdt = binanceFuturesRestApi.positionInformationV3("BTCUSDT", null);
		System.out.println(btcusdt.getData().toJson());
	}

	@Autowired
	private Map<String, ExchangeClient> exchangeClientMap;
	@Test
	public void test5() {

		ApiResponse<KlineCandlestickDataResponse> btcusdt = binanceFuturesRestApi.klineCandlestickData("BTCUSDT", Interval.INTERVAL_5m, null, null, 20L);
		System.out.println(btcusdt.getData().toJson());
		ExchangeClient binance = exchangeClientMap.get("binance");
		List<KLine> kLines = binance.getFuturesKLine("BTC", ExchangeIntervalEnum.INTERVAL_5m, 20);
		System.out.println(kLines);
	}

	@Test
	public void test6() {
		ApiResponse<OpenInterestResponse> btcusdt = binanceFuturesRestApi.openInterest("BTCUSDT");
		System.out.println(btcusdt.getData().toJson());
		System.out.println(btcusdt.getData().getOpenInterest());
	}

	@Test
	public void test7() {
		ApiResponse<GetFundingRateHistoryResponse> btcusdt = binanceFuturesRestApi.getFundingRateHistory("BTCUSDT", null, null, 5L);
		System.out.println(btcusdt.getData().toJson());
	}

	@Test
	public void test8() {

		ApiResponse<PositionInformationV2Response> btcusdt = binanceFuturesRestApi.positionInformationV2("BTCUSDT", null);
		System.out.println(btcusdt.getData().toJson());
	}

	@Test
	public void test9() {
		ApiResponse<OpenInterestResponse> btcusdt = binanceFuturesRestApi.openInterest("BTCUSDT");
		System.out.println(btcusdt.getData().toJson());
		ApiResponse<OpenInterestStatisticsResponse> btcusdt1 = binanceFuturesRestApi.openInterestStatistics("BTCUSDT", Period.PERIOD_1h, 5L, null, null);
		System.out.println(btcusdt1.getData().toJson());
	}

	@Test
	public void test10() {
		ExchangeClient binance = exchangeClientMap.get("binance");
		BigDecimal btc = binance.getAvgOpenInterest("BTC");
		System.out.println(btc);

		BigDecimal btc1 = binance.getOpenInterest("BTC");
		System.out.println(btc1);
	}

	@Test
	public void test11() {
		ApiResponse<SymbolPriceTickerV2Response> btcusdt = binanceFuturesRestApi.symbolPriceTickerV2("BTCUSDT");
		System.out.println(btcusdt.getData().toJson());
		ExchangeClient binance = exchangeClientMap.get("binance");
		BigDecimal price = binance.getCurrentPrice("BTC");
		System.out.println(price);
	}

}
