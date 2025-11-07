package com.dodo.ai_trader;

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
}
