package com.cping.springbootmall.dao;

import com.cping.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);

}
