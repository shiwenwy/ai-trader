package com.dodo.ai_trader.service.repository.impl;

import cn.hutool.json.JSONUtil;
import com.dodo.ai_trader.service.enums.BillBizTypeEnum;
import com.dodo.ai_trader.service.enums.BillStatusEnum;
import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import com.dodo.ai_trader.service.mapper.AccountBillMapper;
import com.dodo.ai_trader.service.mapper.entity.AccountBillEntity;
import com.dodo.ai_trader.service.model.Account;
import com.dodo.ai_trader.service.model.AccountBill;
import com.dodo.ai_trader.service.repository.AccountBillRepository;
import com.dodo.ai_trader.service.repository.AccountRepository;
import com.dodo.ai_trader.service.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.Map;


@Repository
public class AccountBillRepositoryImpl implements AccountBillRepository {

    @Autowired
    private AccountBillMapper accountBillMapper;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public AccountBill queryAccountBill(String orderId, BillBizTypeEnum bizType) {
        return convertToModel(accountBillMapper.queryAccountBillByOrderId(orderId, bizType.getCode()));
    }

    @Override
    @Transactional
    public void insertBillAndUpdateAccount(AccountBill bill, Account account) {

        int insert = accountBillMapper.insert(convertToEntity(bill));
        AssertUtil.isTrue(insert == 1, ErrorCodeEnum.DATABASE_ERROR, "创建账户流水信息失败", true);

        accountRepository.updateAccountBalance(account);
    }

    @Override
    @Transactional
    public void updateBillAndUpdateAccount(AccountBill bill, Account account) {

        int i = accountBillMapper.updateStatus(bill.getOrderId(), bill.getBizType().getCode(),
                bill.getStatus().getCode(), bill.getVersion());
        AssertUtil.isTrue(i == 1, ErrorCodeEnum.DATABASE_ERROR, "更新账户流水信息失败", true);
        bill.setVersion(bill.getVersion() + 1);
        accountRepository.updateAccountBalance(account);
    }

    private AccountBill convertToModel(AccountBillEntity entity) {
        if (entity == null) {
            return null;
        }
        AccountBill model = new AccountBill();
        model.setUserId(entity.getUserId());
        model.setCardId(entity.getCardId());
        model.setOrderId(entity.getOrderId());
        model.setBizType(BillBizTypeEnum.getByCode(entity.getBizType()));
        model.setOrderTime(entity.getOrderTime());
        model.setAmount(entity.getAmount());
        model.setCurrency(entity.getCurrency());
        model.setStatus(BillStatusEnum.getByCode(entity.getStatus()));
        model.setContext(StringUtils.isBlank(entity.getContext()) ?
                null : JSONUtil.toBean(entity.getContext(), Map.class));
        model.setVersion(entity.getVersion());
        return model;
    }

    private AccountBillEntity convertToEntity(AccountBill model) {
        if (model == null) {
            return null;
        }
        AccountBillEntity entity = new AccountBillEntity();
        entity.setUserId(model.getUserId());
        entity.setCardId(model.getCardId());
        entity.setOrderId(model.getOrderId());
        entity.setBizType(model.getBizType() == null ? null : model.getBizType().getCode());
        entity.setOrderTime(model.getOrderTime());
        entity.setAmount(model.getAmount());
        entity.setCurrency(model.getCurrency());
        entity.setStatus(model.getStatus() == null ? null : model.getStatus().getCode());
        entity.setContext(CollectionUtils.isEmpty(model.getContext()) ?
                null : JSONUtil.toJsonStr(model.getContext()));
        entity.setVersion(model.getVersion());
        return entity;
    }
}
