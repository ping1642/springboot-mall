package com.cping.springbootmall.dao.impl;

import com.cping.springbootmall.dao.ProductDao;
import com.cping.springbootmall.model.Product;
import com.cping.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    // 注入NamedParameterJdbcTemplate
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Product getProductById(Integer productId) {

        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date " +
                "FROM product WHERE product_id = :productId";

        Map<String,Object> map = new HashMap<>();
        map.put("productId" ,productId);

        // (sql,map,RowMapper)
        // RowMapper主要是將資料庫所查詢的數據轉成「Java object」
        // 接住「query」的返回值，然後回傳回去
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());


        if (productList.size() > 0) {
            // 不為空，回傳唯一值(以id查詢)
            return productList.get(0);
        } else {
            // 為空，回傳null
            return null;
        }

    }
}
