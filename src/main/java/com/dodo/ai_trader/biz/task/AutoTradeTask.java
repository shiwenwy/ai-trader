package com.dodo.ai_trader.biz.task;

import com.dodo.ai_trader.service.constant.SystemConstant;
import com.dodo.ai_trader.service.model.DecisionContext;
import com.dodo.ai_trader.service.model.DecisionResult;
import com.dodo.ai_trader.service.model.Signal;
import com.dodo.ai_trader.service.repository.DecisionResultRepository;
import com.dodo.ai_trader.service.service.DataContextService;
import com.dodo.ai_trader.service.service.DecisionService;
import com.dodo.ai_trader.service.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class AutoTradeTask {

    @Autowired
    private DataContextService dataContextService;
    @Autowired
    private DecisionService decisionService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private DecisionResultRepository decisionResultRepository;

    @Scheduled(cron = "5 */15 * * * ?")
    public void binanceAutoTrade() {
        System.out.println("开始执行自动交易任务...");
        DecisionContext context = dataContextService.getDecisionContext(SystemConstant.DEFAULT_USER_ID, "binance");
        DecisionResult decisionResult = decisionService.decide(context);
        decisionResultRepository.save(decisionResult);
        tradeService.executeStrategy(decisionResult);
        System.out.println("自动交易任务执行完毕！");
    }
}
