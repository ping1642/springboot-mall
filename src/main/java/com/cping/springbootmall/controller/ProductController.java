package com.cping.springbootmall.controller;

import com.cping.springbootmall.dto.ProductRequest;
import com.cping.springbootmall.model.Product;
import com.cping.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    // 注入ProductService
    @Autowired
    private ProductService productService;

    // 依id條件查詢
    @GetMapping("/products/{productId}")
    // PathVariable 將路徑「{productId}」傳進參數「productId」
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {

        // 取得商品數據，需使用ProductService內的方法
        Product product = productService.getProductById(productId);

        if (product != null) {
            // 資料不為空，回傳狀態碼200，body為查詢值
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            // 資料為空，回傳狀態碼404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 新增商品
    // 新創建class專門負責去接住前端所傳過來的json參數，避免讓原本的class(Product)太過複雜
    // 記得參數要加「@Valid」，這樣新的class「ProductRequest」的「@NotNull」才會生效
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {

        // 會去資料庫創建商品
        // 要去返回資料庫所生成的productId回來
        Integer productId = productService.createProduct(productRequest);

        // 查詢商品數據回來
        Product product = productService.getProductById(productId);

        // 回給前端201
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
}
