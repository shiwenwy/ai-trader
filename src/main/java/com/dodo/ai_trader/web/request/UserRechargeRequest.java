package com.dodo.ai_trader.web.request;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class UserRechargeRequest implements Serializable {

    private static final long serialVersionUID = 2017736313796369616L;

    private String orderId;

    private String userId;

    private String cardId;

    private BigDecimal amount;

    private String currency;
}
