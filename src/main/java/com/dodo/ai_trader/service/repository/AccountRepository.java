package com.dodo.ai_trader.service.repository;


import com.dodo.ai_trader.service.model.Account;
import java.util.List;

public interface AccountRepository {

    /**
     * 创建账户
     *
     * @param account
     */
    void createAccount(Account account);

    /**
     * 获取账户
     *
     * @param userId
     * @param cardId
     * @param currency
     * @return
     */
    Account getAccount(String userId, String cardId, String currency);

    /**
     * 更新账户余额
     *
     * @param account
     */
    void updateAccountBalance(Account account);

    /**
     * 获取账户列表
     *
     * @param userId
     * @param cardId
     * @return
     */
    List<Account> getAccountListByUserIdAndCardId(String userId, String cardId);
}
