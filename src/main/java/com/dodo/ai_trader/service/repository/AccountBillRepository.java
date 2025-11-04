package com.dodo.ai_trader.service.repository;


import com.dodo.ai_trader.service.enums.BillBizTypeEnum;
import com.dodo.ai_trader.service.model.Account;
import com.dodo.ai_trader.service.model.AccountBill;

public interface AccountBillRepository {

    /**
     * 查询账单
     *
     * @param orderId
     * @param bizType
     * @return
     */
    AccountBill queryAccountBill(String orderId, BillBizTypeEnum bizType);

    /**
     * 充值
     *
     * @param bill
     * @param account
     */
    void insertBillAndUpdateAccount(AccountBill bill, Account account);

    /**
     * 更新账单和余额
     *
     * @param bill
     * @param account
     */
    void updateBillAndUpdateAccount(AccountBill bill, Account account);
}
