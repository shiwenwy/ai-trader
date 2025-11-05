package com.dodo.ai_trader.service.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
@ToString
public class DecisionContext implements Serializable {

    private static final long serialVersionUID = 4593341933507032710L;

    private String userId;

    private String exchange;

    private BigDecimal initTotalBalance;

    private Double sharpeRatio;

    private Map<String, TokenIndicator> tokenIndicatorsMap;
}
