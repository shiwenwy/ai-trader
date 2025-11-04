package com.dodo.ai_trader.service.service.impl;

import com.dodo.ai_trader.service.enums.AccountStatusEnum;
import com.dodo.ai_trader.service.enums.BillBizTypeEnum;
import com.dodo.ai_trader.service.enums.BillStatusEnum;
import com.dodo.ai_trader.service.model.Account;
import com.dodo.ai_trader.service.model.AccountBill;
import com.dodo.ai_trader.service.repository.AccountBillRepository;
import com.dodo.ai_trader.service.repository.AccountRepository;
import com.dodo.ai_trader.service.service.AccountBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AccountBillServiceImpl implements AccountBillService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountBillRepository accountBillRepository;

    @Override
    public void recharge(String orderId, String userId, String cardId, BigDecimal amount, String currency) {

        AccountBill old = accountBillRepository.queryAccountBill(orderId, BillBizTypeEnum.RECHARGE);
        if (old != null) {
            return;
        }
        Account account = accountRepository.getAccount(userId, cardId, currency);
        if (account == null) {
            account = buildNewAccount(userId, cardId, currency);
            accountRepository.createAccount(account);
        }
        account.recharge(amount);

        AccountBill accountBill = buildNewAccountBill(orderId, BillBizTypeEnum.RECHARGE, userId, cardId, amount, currency);

        accountBillRepository.insertBillAndUpdateAccount(accountBill, account);
    }

    private AccountBill buildNewAccountBill(String orderId, BillBizTypeEnum bizType, String userId,
                                            String cardId, BigDecimal amount, String currency) {
        AccountBill accountBill = new AccountBill();
        accountBill.setOrderId(orderId);
        accountBill.setBizType(bizType);
        accountBill.setOrderTime(new Date());
        accountBill.setAmount(amount);
        accountBill.setCurrency(currency);
        accountBill.setStatus(BillStatusEnum.SUCCESS);
        accountBill.setUserId(userId);
        accountBill.setCardId(cardId);
        accountBill.setVersion(1);
        return accountBill;
    }

    private Account buildNewAccount(String userId, String cardId, String currency) {
        Account account = new Account();
        account.setUserId(userId);
        account.setAccountName(cardId + "账户");
        account.setCardId(cardId);
        account.setCurrency(currency);
        account.setBalance(BigDecimal.ZERO);
        account.setAvailableBalance(BigDecimal.ZERO);
        account.setFrozenBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatusEnum.NORMAL);
        account.setVersion(1L);
        return account;
    }
}
