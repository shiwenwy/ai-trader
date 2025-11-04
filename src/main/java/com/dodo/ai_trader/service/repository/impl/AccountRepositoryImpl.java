package com.dodo.ai_trader.service.repository.impl;

import com.dodo.ai_trader.service.enums.AccountStatusEnum;
import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import com.dodo.ai_trader.service.mapper.AccountMapper;
import com.dodo.ai_trader.service.mapper.entity.AccountEntity;
import com.dodo.ai_trader.service.model.Account;
import com.dodo.ai_trader.service.repository.AccountRepository;
import com.dodo.ai_trader.service.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void createAccount(Account account) {
        int insert = accountMapper.insert(convertToEntity(account));
        AssertUtil.isTrue(insert == 1, ErrorCodeEnum.DATABASE_ERROR, "创建账户信息失败", true);
    }

    @Override
    public Account getAccount(String userId, String cardId, String currency) {

        return convertToModel(accountMapper.queryAccount(userId, cardId, currency));
    }

    @Override
    public void updateAccountBalance(Account account) {
        int update = accountMapper.updateAmount(convertToEntity(account));
        AssertUtil.isTrue(update == 1, ErrorCodeEnum.DATABASE_ERROR, "更新账户信息失败", true);
        account.setVersion(account.getVersion() + 1);
    }

    @Override
    public List<Account> getAccountListByUserIdAndCardId(String userId, String cardId) {

        List<AccountEntity> accountEntityList = accountMapper.getAccountListByUserIdAndCardId(userId, cardId);
        if (accountEntityList != null && !accountEntityList.isEmpty()) {
            return accountEntityList.stream().map(this::convertToModel).toList();
        }
        return null;
    }

    private AccountEntity convertToEntity(Account model) {
        if (model == null) {
            return null;
        }
        AccountEntity entity = new AccountEntity();
        entity.setUserId(model.getUserId());
        entity.setAccountName(model.getAccountName());
        entity.setCardId(model.getCardId());
        entity.setCurrency(model.getCurrency());
        entity.setBalance(model.getBalance());
        entity.setFrozenBalance(model.getFrozenBalance());
        entity.setAvailableBalance(model.getAvailableBalance());
        entity.setStatus(model.getStatus() == null ? null : model.getStatus().getCode());
        entity.setVersion(model.getVersion());
        return entity;
    }

    private Account convertToModel(AccountEntity entity) {
        if (entity == null) {
            return null;
        }
        Account model = new Account();
        model.setUserId(entity.getUserId());
        model.setAccountName(entity.getAccountName());
        model.setCardId(entity.getCardId());
        model.setCurrency(entity.getCurrency());
        model.setBalance(entity.getBalance());
        model.setFrozenBalance(entity.getFrozenBalance());
        model.setAvailableBalance(entity.getAvailableBalance());
        model.setStatus(AccountStatusEnum.getByCode(entity.getStatus()));
        model.setVersion(entity.getVersion());
        return model;
    }
}
