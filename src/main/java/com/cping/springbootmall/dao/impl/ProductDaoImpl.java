package com.cping.springbootmall.dao.impl;

import com.cping.springbootmall.dao.ProductDao;
import com.cping.springbootmall.dto.ProductRequest;
import com.cping.springbootmall.model.Product;
import com.cping.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.swing.plaf.basic.BasicTableUI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    // 注入NamedParameterJdbcTemplate
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // 依id查詢
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

    // 新增商品
    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product(product_name, category, image_url, price, stock," +
                " description, created_date, last_modified_date) " +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description, " +
                ":createdDate, :lastModifiedDate)";

        // 前端所傳來的參數，一個一個加到map裡
        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        // 當下的時間點
        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        // 使用KeyHolder 去儲存資料庫自動生成的productId
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int productId = keyHolder.getKey().intValue();

        // 然後productId回傳
        return productId;
    }
}
