package com.cping.springbootmall.dao.impl;

import com.cping.springbootmall.constant.ProductCategory;
import com.cping.springbootmall.dao.ProductDao;
import com.cping.springbootmall.dto.ProductQueryParams;
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

    // 計算商品總筆數
    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql = "SELECT count(*) FROM product WHERE 1=1";

        Map<String,Object> map = new HashMap<>();

        // 查詢條件(將共同程式獨立出來使用)
        sql = addFilteringSql(sql, map, productQueryParams);

        // Integer.class是將count值轉換成Integer類型的返回值
        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return total;
    }

    // 查詢商品列表
    // 依category條件去查詢(WHERE 1=1主要是可以自由的去拼接sql語法的後面)
    // WHERE 1=1 不會造成查詢影響
    // 依關鍵字條件去查詢
    // 一併將參數值傳出去
    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date " +
                "FROM product WHERE 1=1";

        Map<String,Object> map = new HashMap<>();

        // 查詢條件(將共同程式獨立出來使用)
        sql = addFilteringSql(sql, map, productQueryParams);

        // 排序
        // 使用ORDER BY 只能用拼接的方式
        sql = sql + " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

        // 分頁
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", productQueryParams.getLimit());
        map.put("offset", productQueryParams.getOffset());

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        return productList;

    }

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

    // 修改商品
    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name = :productName, category = :category, image_url = :imageUrl, " +
                "price = :price, stock = :stock, description = :description, last_modified_date = :lastModifiedDate " +
                "WHERE product_id = :productId";

        Map<String,Object> map = new HashMap<>();
        map.put("productId", productId);

        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    // 扣除商品庫存
    @Override
    public void updateStock(Integer productId, Integer stock) {

        String sql = "UPDATE product SET stock = :stock, last_modified_date = :lastModifiedDate " +
                "WHERE product_id = :productId";

        Map<String,Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("stock", stock);
        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    // 刪除商品
    @Override
    public void deleteProductById(Integer productId) {

        String sql = "DELETE FROM product WHERE product_id = :productId";

        Map<String,Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql,map);
    }

    // 提煉程式：將重複的程式碼獨立出來成一個方法，讓其他方法共同使用此方法
    private String addFilteringSql(String sql, Map<String,Object> map, ProductQueryParams productQueryParams) {
        // 依category條件去查詢
        if (productQueryParams.getCategory() != null) {
            sql = sql + " AND category = :category";
            // 將前端傳過來商品種類的值(category)加到map內
            // .name() 是將Enum類型轉成字串
            map.put("category", productQueryParams.getCategory().name());
        }

        // 依關鍵字條件去查詢
        if (productQueryParams.getSearch() != null) {
            sql = sql + " AND product_name LIKE :search";
            // 「%」一定要寫在map裡，再根據變數傳進去
            map.put("search", "%" + productQueryParams.getSearch() + "%");
        }

        return sql;
    }


}
