package com.dodo.ai_trader.biz.task;


import com.dodo.ai_trader.service.client.ExchangeClient;
import com.dodo.ai_trader.service.constant.SystemConstant;
import com.dodo.ai_trader.service.model.ExchangeBalanceSnap;
import com.dodo.ai_trader.service.model.User;
import com.dodo.ai_trader.service.model.market.ExchangeBalance;
import com.dodo.ai_trader.service.repository.ExchangeBalanceSnapRepository;
import com.dodo.ai_trader.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@EnableScheduling
public class ExchangeSyncTask {

    @Autowired
    private ExchangeBalanceSnapRepository exchangeBalanceSnapRepository;
    @Autowired
    private Map<String, ExchangeClient> exchangeClientMap;
    @Autowired
    private UserRepository userRepository;

//    @Scheduled(cron = "0 * * * * ?")
    public void binanceBalanceSync() {
        ExchangeClient binance = exchangeClientMap.get("binance");
        ExchangeBalance balance = binance.getBalance();
        if (balance == null) {
            return;
        }
        User user = userRepository.getUserById(SystemConstant.DEFAULT_USER_ID);
        exchangeBalanceSnapRepository.save(buildExchangeBalanceSnap(user, balance));
    }

    private ExchangeBalanceSnap buildExchangeBalanceSnap(User user, ExchangeBalance balance) {
        ExchangeBalanceSnap exchangeBalanceSnap = new ExchangeBalanceSnap();
        exchangeBalanceSnap.setUserId(user.getUserId());
        exchangeBalanceSnap.setExchange("binance");
        exchangeBalanceSnap.setTotalEquity(balance.calculateTotalEquity());
        exchangeBalanceSnap.setTotalWalletBalance(balance.getTotalWalletBalance());
        exchangeBalanceSnap.setTotalUnrealizedProfit(balance.getTotalUnrealizedProfit());
        exchangeBalanceSnap.setAvailableBalance(balance.getAvailableBalance());
        exchangeBalanceSnap.setTimestamp(System.currentTimeMillis());
        return exchangeBalanceSnap;
    }
}
