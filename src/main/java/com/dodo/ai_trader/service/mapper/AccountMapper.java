package com.dodo.ai_trader.service.mapper;

import com.dodo.ai_trader.service.mapper.entity.AccountEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMapper {

    /**
     * 添加用户账户信息
     *
     * @param record
     * @return
     */
    int insert(AccountEntity record);

    /**
     * 更新用户账户金额
     *
     * @param record
     * @return
     */
    int updateAmount(AccountEntity record);

    /**
     * 更新用户账户状态
     *
     * @param userId
     * @param cardId
     * @param currency
     * @param status
     * @param version
     * @return
     */
    int updateStatus(String userId, String cardId, String currency, String status, Long version);

    /**
     * 查询用户账户信息
     *
     * @param userId
     * @param cardId
     * @param currency
     * @return
     */
    AccountEntity queryAccount(@Param("userId") String userId, @Param("cardId") String cardId, @Param("currency") String currency);

    /**
     * 查询用户账户列表
     *
     * @param userId
     * @param cardId
     * @return
     */
    List<AccountEntity> getAccountListByUserIdAndCardId(@Param("userId") String userId, @Param("cardId") String cardId);
}
