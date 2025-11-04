package com.dodo.ai_trader.service.mapper;

import com.dodo.ai_trader.service.mapper.entity.AccountBillEntity;
import org.apache.ibatis.annotations.Param;

public interface AccountBillMapper {

    /**
     * 添加账单
     * @param record
     * @return
     */
    int insert(AccountBillEntity record);

    /**
     * 查询账单
     * @param orderId
     * @param bizType
     * @return
     */
    AccountBillEntity queryAccountBillByOrderId(@Param("orderId") String orderId, @Param("bizType") String bizType);

    /**
     * 更新账单状态
     * @param orderId
     * @param bizType
     * @param status
     * @param version
     * @return
     */
    int updateStatus(@Param("orderId") String orderId, @Param("bizType") String bizType, @Param("status") String status, @Param("version") Integer version);
}
