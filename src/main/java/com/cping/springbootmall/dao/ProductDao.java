package com.cping.springbootmall.dao;

import com.cping.springbootmall.constant.ProductCategory;
import com.cping.springbootmall.dto.ProductQueryParams;
import com.cping.springbootmall.dto.ProductRequest;
import com.cping.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    // 計算商品總筆數
    Integer countProduct (ProductQueryParams productQueryParams);

    // 查詢商品列表
    // 依category條件去查詢
    // 依關鍵字條件去查詢
    // 一併將參數值傳出去
    List<Product> getProducts(ProductQueryParams productQueryParams);

    // 依id查詢
    Product getProductById(Integer productId);

    // 新增商品
    Integer createProduct(ProductRequest productRequest);

    // 修改商品
    void updateProduct(Integer productId, ProductRequest productRequest);

    // 刪除商品
    void deleteProductById(Integer productId);
}
