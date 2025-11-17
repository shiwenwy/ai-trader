package com.dodo.ai_trader.service.model;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class DecisionResult implements Serializable {

    private static final long serialVersionUID = 1604654688585598306L;

    private String userId;

    private String exchange;

    private String thinking;

    private List<Signal> signalList;

    private Long timestamp;

    public boolean betweenMinute(int minutes) {
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis - timestamp < minutes * 60 * 1000;
    }
}
