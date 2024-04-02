package com.cping.springbootmall.dao;

import com.cping.springbootmall.dto.ProductRequest;
import com.cping.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    // 查詢商品列表
    List<Product> getProducts();

    // 依id查詢
    Product getProductById(Integer productId);

    // 新增商品
    Integer createProduct(ProductRequest productRequest);

    // 修改商品
    void updateProduct(Integer productId, ProductRequest productRequest);

    // 刪除商品
    void deleteProductById(Integer productId);
}
