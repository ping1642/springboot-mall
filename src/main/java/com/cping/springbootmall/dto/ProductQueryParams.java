package com.cping.springbootmall.dto;

import com.cping.springbootmall.constant.ProductCategory;

// 此class主要存放查詢條件的參數
public class ProductQueryParams {

    private ProductCategory category;
    private String search;

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}