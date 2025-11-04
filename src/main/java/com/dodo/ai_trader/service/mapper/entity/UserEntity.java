package com.dodo.ai_trader.service.mapper.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: shiwen
 * Date: 2025/4/24 20:49
 * Description:
 */
@Data
@ToString
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -4351029122264776338L;

    /**
     * ID
     */
    private Integer id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
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
    private String status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 上下文
     */
    private String context;
    /**
     * 版本
     */
    private Integer version;
}
