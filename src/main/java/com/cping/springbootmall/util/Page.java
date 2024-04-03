package com.cping.springbootmall.util;

import java.util.List;

// 處理分頁相關的 response
public class Page<T> {

    // 想要返回的值
    private Integer limit;
    private Integer offset;
    // 總筆數
    private Integer total;
    // 存放商品數據
    private List<T> results;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
