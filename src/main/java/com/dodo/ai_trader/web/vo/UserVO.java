package com.dodo.ai_trader.web.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserVO implements Serializable {

    private static final long serialVersionUID = 466421217958069358L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名,用于登陆交易平台
     */
    private String email;

    /**
     * 用户状态
     */
    private String status;
}
