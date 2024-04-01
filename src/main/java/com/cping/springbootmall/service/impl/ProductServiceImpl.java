package com.cping.springbootmall.service.impl;

import com.cping.springbootmall.dao.ProductDao;
import com.cping.springbootmall.model.Product;
import com.cping.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    // 注入ProductDao
    @Autowired
    private ProductDao productDao;

    @Override
    public Product getProductById(Integer productId) {

        return productDao.getProductById(productId);

    }
}
