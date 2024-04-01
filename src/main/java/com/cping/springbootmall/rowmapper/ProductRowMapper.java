package com.cping.springbootmall.rowmapper;

import com.cping.springbootmall.constant.ProductCategory;
import com.cping.springbootmall.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();

        // 從「ResultSet」的參數去取得資料庫查詢出來的數據，然後存在「Product」變數內
        // 這樣就可以取得各欄位的值
        product.setProductId(rs.getInt("product_id"));
        product.setProductName(rs.getString("product_name"));

        // 因category從String類型改成「ProductCategory」類型，會出錯
        // 1. 先使用String類型接住資料庫取出的值
        String categoryStr = rs.getString("category");
        // 2. 將字串轉換成「Enum」類型
        ProductCategory category = ProductCategory.valueOf(categoryStr);
        // 3. 將category變數傳入set方法內
        product.setCategory(category);

        product.setImageUrl(rs.getString("image_url"));
        product.setPrice(rs.getInt("price"));
        product.setStock(rs.getInt("stock"));
        product.setDescription(rs.getString("description"));
        product.setCreatedDate(rs.getTimestamp("created_date"));
        product.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

        return product;
    }
}
