package com.dodo.ai_trader.web.utils;

import com.dodo.ai_trader.service.model.User;
import com.dodo.ai_trader.web.vo.UserVO;

public class ControllerUtil {

    public static UserVO buildUserVO(User user) {
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getUserId());
        userVO.setEmail(user.getEmail());
        userVO.setStatus(user.getStatus() == null ? null : user.getStatus().getCode());
        return userVO;
    }
}
