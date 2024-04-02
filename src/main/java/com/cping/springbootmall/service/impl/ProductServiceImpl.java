package com.cping.springbootmall.service.impl;

import com.cping.springbootmall.dao.ProductDao;
import com.cping.springbootmall.dto.ProductRequest;
import com.cping.springbootmall.model.Product;
import com.cping.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    // 注入ProductDao
    @Autowired
    private ProductDao productDao;

    // 依id查詢
    @Override
    public Product getProductById(Integer productId) {

        return productDao.getProductById(productId);

    }

    // 新增商品
    @Override
    public Integer createProduct(ProductRequest productRequest) {
        return productDao.createProduct(productRequest);
    }

    // 修改商品
    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId, productRequest);
    }
}
