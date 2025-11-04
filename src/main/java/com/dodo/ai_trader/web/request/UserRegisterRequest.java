package com.dodo.ai_trader.web.request;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -7345837158791479392L;

    /**
     * 用户名,用于登陆交易平台
     */
    private String email;

    /**
     * 密码
     */
    private String password;
}
