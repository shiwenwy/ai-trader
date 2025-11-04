package com.dodo.ai_trader.web.validator;

import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import com.dodo.ai_trader.service.utils.AssertUtil;
import com.dodo.ai_trader.web.request.UserRechargeRequest;
import com.dodo.ai_trader.web.request.UserRegisterRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateRegister(UserRegisterRequest request) {
        AssertUtil.notNull(request, ErrorCodeEnum.PARAM_ERROR, "请求参数为空", false);
        AssertUtil.notEmpty(request.getEmail(), ErrorCodeEnum.PARAM_ERROR, "email为空", false);
        AssertUtil.notEmpty(request.getPassword(), ErrorCodeEnum.PARAM_ERROR, "密码为空", false);
    }

    public void validateUserRechargeRequest(UserRechargeRequest request) {
        AssertUtil.notNull(request, ErrorCodeEnum.PARAM_ERROR, "请求参数为空", false);
        AssertUtil.notEmpty(request.getOrderId(), ErrorCodeEnum.PARAM_ERROR, "订单ID为空", false);
        AssertUtil.notEmpty(request.getUserId(), ErrorCodeEnum.PARAM_ERROR, "用户ID为空", false);
        AssertUtil.notEmpty(request.getCardId(), ErrorCodeEnum.PARAM_ERROR, "卡ID为空", false);
        AssertUtil.isTrue(StringUtils.equals(request.getCardId(), "binance"), ErrorCodeEnum.PARAM_ERROR, "暂不支持该类型卡", false);
        AssertUtil.notNull(request.getAmount(), ErrorCodeEnum.PARAM_ERROR, "金额不能为空", false);
        AssertUtil.notEmpty(request.getCurrency(), ErrorCodeEnum.PARAM_ERROR, "币种为空", false);
        AssertUtil.isTrue(StringUtils.equals(request.getCurrency(), "USDT"), ErrorCodeEnum.PARAM_ERROR, "暂不支持该币种", false);
    }
}
