package com.dodo.ai_trader.service.model;

import com.dodo.ai_trader.service.enums.PositionOrderStatus;
import com.dodo.ai_trader.service.enums.SideEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
public class OpenPositionOrder implements Serializable {

    private static final long serialVersionUID = 2811676517339825246L;

    private String clientOrderId;

    private String orderId;

    private String userId;

    private String exchange;

    private String symbol;

    private SideEnum side;

    private String type;

    private String timeInForce;

    private BigDecimal quantity;

    private BigDecimal entryPrice;

    private BigDecimal stopPrice;

    private BigDecimal profitTarget;

    private Date orderTime;

    private PositionOrderStatus status;

    private Integer version;
}
