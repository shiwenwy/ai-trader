package com.dodo.ai_trader;

import com.dodo.ai_trader.service.decision.BinanceDecision;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
