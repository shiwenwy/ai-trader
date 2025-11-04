package com.dodo.ai_trader.service.model;

import com.dodo.ai_trader.service.enums.UserStatusEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Author: shiwen
 * Date: 2025/4/25 17:52
 * Description:
 */
@Data
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 532403052656624820L;


    private Date createTime;

    private Date updateTime;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 密码
     */
    private String password;
    /**
     * 支付密码
     */
    private String payPassword;
    /**
     * 盐
     */
    private String salt;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 状态
     */
    private UserStatusEnum status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 上下文
     */
    private Map<String, Object> context;
    /**
     * 版本
     */
    private Integer version;
}
