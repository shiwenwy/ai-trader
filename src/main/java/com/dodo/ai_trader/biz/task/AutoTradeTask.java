package com.dodo.ai_trader.biz.task;

import com.dodo.ai_trader.service.constant.SystemConstant;
import com.dodo.ai_trader.service.model.DecisionContext;
import com.dodo.ai_trader.service.model.Signal;
import com.dodo.ai_trader.service.service.DataContextService;
import com.dodo.ai_trader.service.service.DecisionService;
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

    @Scheduled(cron = "0 */15 * * * ?")
    public void binanceAutoTrade() {

        DecisionContext context = dataContextService.getDecisionContext(SystemConstant.DEFAULT_USER_ID, "binance");
        List<Signal> signalList = decisionService.decide(context);
    }
}
