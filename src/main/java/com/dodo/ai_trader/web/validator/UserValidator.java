package com.dodo.ai_trader.web.validator;

import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import com.dodo.ai_trader.service.utils.AssertUtil;
import com.dodo.ai_trader.web.request.UserRegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateRegister(UserRegisterRequest request) {
        AssertUtil.notNull(request, ErrorCodeEnum.PARAM_ERROR, "请求参数为空", false);
        AssertUtil.notEmpty(request.getEmail(), ErrorCodeEnum.PARAM_ERROR, "email为空", false);
        AssertUtil.notEmpty(request.getPassword(), ErrorCodeEnum.PARAM_ERROR, "密码为空", false);
    }
}
