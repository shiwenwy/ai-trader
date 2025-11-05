package com.dodo.ai_trader.service.repository.impl;


import cn.hutool.json.JSONUtil;
import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import com.dodo.ai_trader.service.enums.UserStatusEnum;
import com.dodo.ai_trader.service.mapper.UserMapper;
import com.dodo.ai_trader.service.mapper.entity.UserEntity;
import com.dodo.ai_trader.service.model.User;
import com.dodo.ai_trader.service.repository.AsyncTaskRepository;
import com.dodo.ai_trader.service.repository.UserRepository;
import com.dodo.ai_trader.service.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AsyncTaskRepository asyncTaskRepository;

    @Override
    public User getUserById(String userId) {
        return convertToModel(userMapper.getUserById(userId));
    }

    @Override
    public void addUser(User user) {
        int insert = userMapper.insert(convertToEntity(user));
        AssertUtil.isTrue(insert == 1, ErrorCodeEnum.DATABASE_ERROR, "创建用户信息失败", true);
    }

    @Override
    public User getUserByUserName(String email) {
        return convertToModel(userMapper.getUserByEmail(email));
    }

    @Override
    public List<User> getAllEnableUsers() {
        List<UserEntity> allEnableUsers = userMapper.getAllEnableUsers();
        if (!CollectionUtils.isEmpty(allEnableUsers)) {
            return allEnableUsers.stream().map(this::convertToModel).toList();
        }
        return null;
    }


    private UserEntity convertToEntity(User user) {
        if (user == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(user.getUserId());
        userEntity.setPassword(user.getPassword());
        userEntity.setEmail(user.getEmail());
        userEntity.setSalt(user.getSalt());
        userEntity.setPayPassword(user.getPayPassword());
        userEntity.setSalt(user.getSalt());
        userEntity.setRemark(user.getRemark());
        userEntity.setNickName(user.getNickName());
        userEntity.setStatus(user.getStatus() == null ? null : user.getStatus().getCode());
        userEntity.setContext(CollectionUtils.isEmpty(user.getContext()) ?
                null : JSONUtil.toJsonStr(user.getContext()));
        userEntity.setVersion(user.getVersion());
        return userEntity;
    }

    private User convertToModel(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        User user = new User();
        user.setCreateTime(userEntity.getCreateTime());
        user.setUpdateTime(userEntity.getUpdateTime());
        user.setUserId(userEntity.getUserId());
        user.setNickName(userEntity.getNickName());
        user.setPassword(userEntity.getPassword());
        user.setPayPassword(userEntity.getPayPassword());
        user.setSalt(userEntity.getSalt());
        user.setEmail(userEntity.getEmail());
        user.setRemark(userEntity.getRemark());
        user.setStatus(UserStatusEnum.getByCode(userEntity.getStatus()));
        user.setContext(StringUtils.isBlank(userEntity.getContext()) ?
                null : JSONUtil.toBean(userEntity.getContext(), Map.class));
        user.setVersion(userEntity.getVersion());
        return user;
    }
}
