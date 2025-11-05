package com.dodo.ai_trader.service.mapper;

import com.dodo.ai_trader.service.mapper.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Author: shiwen
 * Date: 2025/4/24 20:21
 * Description:
 */
public interface UserMapper {

    /**
     * 添加用户
     * @param record
     * @return
     */
    int insert(UserEntity record);

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    UserEntity getUserById(@Param("userId") String userId);

    /**
     * 根据邮箱查询用户信息
     * @param email
     * @return
     */
    UserEntity getUserByEmail(@Param("email") String email);

    /**
     * 查询所有用户信息
     * @return
     */
    List<UserEntity> getAllEnableUsers();
}
