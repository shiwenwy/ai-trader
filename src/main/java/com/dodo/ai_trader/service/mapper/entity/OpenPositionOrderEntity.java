package com.dodo.ai_trader.service.mapper.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
public class OpenPositionOrderEntity implements Serializable {

    private static final long serialVersionUID = 7272566071391714141L;

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

    private String clientOrderId;

    private String orderId;

    private String userId;

    private String exchange;

    private String symbol;

    private String side;

    private String type;

    /**
     * 杠杆倍数
     */
    private Integer leverage;

    private String timeInForce;

    private BigDecimal quantity;

    private BigDecimal entryPrice;

    private BigDecimal stopPrice;

    private BigDecimal profitTarget;

    private Date orderTime;

    private String status;

    private Integer version;
}
