package com.cping.springbootmall.dao;

import com.cping.springbootmall.dto.ProductRequest;
import com.cping.springbootmall.model.Product;

public interface ProductDao {

    // 依id查詢
    Product getProductById(Integer productId);

    // 新增商品
    Integer createProduct(ProductRequest productRequest);

}
