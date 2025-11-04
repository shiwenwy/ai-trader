package com.dodo.ai_trader.web.base;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Author: shiwen
 * Date: 2025/5/8 16:36
 * Description:
 */
@Data
@ToString
public class Page<T> implements Serializable {

    private static final long serialVersionUID = -4685035634345736145L;

    private int pageNum;

    private int pageSize;

    private int total;

    private List<T> pages;

    public Page(int pageNum, int pageSize, int total, List<T> pages) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = pages;
    }
}
