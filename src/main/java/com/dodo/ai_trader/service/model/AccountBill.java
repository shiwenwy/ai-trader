package com.dodo.ai_trader.service.model;

import com.dodo.ai_trader.service.enums.BillBizTypeEnum;
import com.dodo.ai_trader.service.enums.BillStatusEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 账户流水
 */
@Data
@ToString
public class AccountBill implements Serializable {

    private static final long serialVersionUID = 2476165363141592524L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 卡ID
     */
    private String cardId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 业务类型
     */
    private BillBizTypeEnum bizType;

    /**
     * 订单时间
     */
    private Date orderTime;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 状态
     */
    private BillStatusEnum status;

    /**
     * 订单上下文
     */
    private Map<String, Object> context;

    /**
     * 版本号
     */
    private Integer version;

    public boolean hasFinished() {
        return BillStatusEnum.SUCCESS == status
                || BillStatusEnum.CANCEL == status;
    }

    public void addContext(String key, Object value) {
        if (context == null) {
            context = new HashMap<>();
        }
        context.put(key, value);
    }

    public Object getContext(String key) {
        if (context == null) {
            return null;
        }
        return context.get(key);
    }
}
