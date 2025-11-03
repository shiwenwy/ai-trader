package com.dodo.ai_trader.biz.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class AutoTradeTask {

    @Scheduled(cron = "0 */15 * * * ?")
    public void binanceAutoTrade() {

    }
}
