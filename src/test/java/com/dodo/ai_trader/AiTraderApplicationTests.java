package com.dodo.ai_trader;

import com.dodo.ai_trader.service.decision.BinanceDecision;
import com.dodo.ai_trader.service.model.CommonPosition;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class AiTraderApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private BinanceDecision binanceDecision;

	@Test
	public void test() {
		String test = binanceDecision.test();
		System.out.println(test);
	}

	@Test
	public void test2() {
		List<CommonPosition> positions = new ArrayList<>();
		CommonPosition btc = new CommonPosition();
		btc.setSymbol("BTC");
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
		CommonPosition eth = new CommonPosition();
		eth.setSymbol("ETH");
		eth.setQuantity(0.5);
		eth.setEntryPrice(2000.0);
		eth.setCurrentPrice(2050.0);
		eth.setLeverage(20);
		eth.setProfitTarget(0.05);
		eth.setStopLoss(0.02);
		eth.setInvalidationCondition("eth_price > 2100");
		eth.setConfidence(0.8);
		positions.add(eth);

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

		Prompt userPrompt = binanceDecision.buildUserPrompt(100, btcData, ethData, solData, bnbData, 0.0, 0.0, 1000000.0, 1000000.0, positions);
		System.out.println(userPrompt.getContents());
	}
}
