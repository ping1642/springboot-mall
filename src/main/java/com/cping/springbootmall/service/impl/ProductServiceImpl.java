package com.cping.springbootmall.service.impl;

import com.cping.springbootmall.constant.ProductCategory;
import com.cping.springbootmall.dao.ProductDao;
import com.cping.springbootmall.dto.ProductQueryParams;
import com.cping.springbootmall.dto.ProductRequest;
import com.cping.springbootmall.model.Product;
import com.cping.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {


    // 注入ProductDao
    @Autowired
    private ProductDao productDao;

    // 查詢商品列表
    // 依category條件去查詢
    // 依關鍵字條件去查詢
    // 一併將參數值傳出去
    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        return productDao.getProducts(productQueryParams);
    }

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

    // 刪除商品

    @Override
    public void deleteProductById(Integer productId) {
        productDao.deleteProductById(productId);
    }
}
