package com.dodo.ai_trader.service.repository;

import com.dodo.ai_trader.service.model.User;

import java.util.List;

public interface UserRepository {

    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     */
    User getUserById(String userId);

    /**
     * 添加用户
     * @param user
     * @return
     */
    void addUser(User user);

    /**
     * 根据商户id和email获取用户信息
     * @param email
     * @return
     */
    User getUserByUserName(String email);

    /**
     * 获取所有启用的用户
     * @return
     */
    List<User> getAllEnableUsers();
}
