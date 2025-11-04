package com.dodo.ai_trader.service.model;

import com.dodo.ai_trader.service.enums.AccountStatusEnum;
import com.dodo.ai_trader.service.enums.ErrorCodeEnum;
import com.dodo.ai_trader.service.utils.AssertUtil;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Author: shiwen
 * Date: 2025/5/17 14:22
 * Description:
 */
@Data
@ToString
public class Account implements Serializable {

    private static final long serialVersionUID = -3652868096674853864L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 卡id
     */
    private String cardId;

    /**
     * 币种
     */
    private String currency;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal frozenBalance;

    /**
     * 可用余额
     */
    private BigDecimal availableBalance;

    /**
     * 状态
     */
    private AccountStatusEnum status;

    /**
     * 版本号
     */
    private Long version;

    public void recharge(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        this.availableBalance = this.availableBalance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
        this.availableBalance = this.availableBalance.subtract(amount);

        AssertUtil.isTrue(this.balance.compareTo(BigDecimal.ZERO) >= 0,
                ErrorCodeEnum.ACCOUNT_NOT_ENOUGH, "账户总余额不足", false);
        AssertUtil.isTrue(this.availableBalance.compareTo(BigDecimal.ZERO) >= 0,
                ErrorCodeEnum.ACCOUNT_NOT_ENOUGH, "账户可用余额不足", false);
    }

    public void freeze(BigDecimal amount) {
        this.availableBalance = this.availableBalance.subtract(amount);
        this.frozenBalance = this.frozenBalance.add(amount);
    }

    public void unfreeze(BigDecimal amount) {
        this.availableBalance = this.availableBalance.add(amount);
        this.frozenBalance = this.frozenBalance.subtract(amount);
    }
}
