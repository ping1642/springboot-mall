package com.cping.springbootmall.dto;

import com.cping.springbootmall.constant.ProductCategory;
import jakarta.validation.constraints.NotNull;


public class ProductRequest {
    // 思考前端會傳哪些參數
    @NotNull
    private String productName;
    // 用String類型儲存，會無法從Spring Boot程式中知道到底有哪些商品的分類
    // 如果對比較底層的class做任何的改動時，一定要檢查「RowMapper」是否需要調整
    @NotNull
    private ProductCategory category;
    @NotNull
    private String imageUrl;
    @NotNull
    private Integer price;
    @NotNull
    private Integer stock;
    private String description;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
