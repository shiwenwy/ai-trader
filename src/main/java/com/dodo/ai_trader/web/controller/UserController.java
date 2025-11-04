package com.dodo.ai_trader.web.controller;

import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import com.dodo.ai_trader.service.enums.UserStatusEnum;
import com.dodo.ai_trader.service.exception.BizException;
import com.dodo.ai_trader.service.model.User;
import com.dodo.ai_trader.service.repository.UserRepository;
import com.dodo.ai_trader.service.service.AccountBillService;
import com.dodo.ai_trader.service.utils.AssertUtil;
import com.dodo.ai_trader.service.utils.IdGenerator;
import com.dodo.ai_trader.service.utils.LogUtil;
import com.dodo.ai_trader.service.utils.SHA256Util;
import com.dodo.ai_trader.web.base.WebResult;
import com.dodo.ai_trader.web.request.UserRechargeRequest;
import com.dodo.ai_trader.web.request.UserRegisterRequest;
import com.dodo.ai_trader.web.utils.ControllerUtil;
import com.dodo.ai_trader.web.validator.UserValidator;
import com.dodo.ai_trader.web.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private AccountBillService accountBillService;

    @PostMapping("/register")
    @ResponseBody
    @Operation(summary = "用户注册")
    public WebResult<UserVO> register(@RequestBody UserRegisterRequest request) {

        userValidator.validateRegister(request);

        request.setEmail(StringUtils.remove(request.getEmail(), " ").toLowerCase());

        User old = userRepository.getUserByUserName(request.getEmail());
        if (old != null) {
            throw new BizException(ErrorCodeEnum.USER_EXIST, false);
        }

        User user = buildNewUser(request);

        userRepository.addUser(user);

        return WebResult.success(ControllerUtil.buildUserVO(user));
    }

    @PostMapping("/recharge")
    @ResponseBody
    @Operation(summary = "用户充值")
    public WebResult<String> recharge(@RequestBody UserRechargeRequest request) {

        userValidator.validateUserRechargeRequest(request);

        User user = userRepository.getUserById(request.getUserId());
        AssertUtil.notNull(user, ErrorCodeEnum.USER_NOT_EXIST, "用户不存在", false);

        accountBillService.recharge(request.getOrderId(), request.getUserId(),
                request.getCardId(), request.getAmount(), request.getCurrency());

        return WebResult.success("充值成功");
    }

    private User buildNewUser(UserRegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setSalt(IdGenerator.generateRandomString());
        String initPassword = request.getPassword();
        try {
            user.setPassword(SHA256Util.sha256WithSalt(initPassword, user.getSalt()));
            user.setPayPassword(SHA256Util.sha256WithSalt(initPassword, user.getSalt()));
        } catch (Exception e) {
            throw new BizException(ErrorCodeEnum.SYSTEM_ERROR, false);
        }
        user.setNickName(request.getEmail());
        user.setStatus(UserStatusEnum.ENABLE);
        user.setUserId(IdGenerator.generateId("U"));
        user.setVersion(1);
        return user;
    }
}
