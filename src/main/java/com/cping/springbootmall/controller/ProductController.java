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

    // 修改商品
    // 記得參數要加「@Valid」，這樣新的class「ProductRequest」的「@NotNull」才會生效
    @PutMapping("products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest) {

        // 1.先查詢要更新的商品有沒有在資料庫內
        Product product = productService.getProductById(productId);

        if (product == null) {
            // 不存在回傳404給前端
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 2.修改商品數據
        // (要更新的是哪一個商品, 商品修改過後的值是什麼)
        // updateProduct不會返回值
        productService.updateProduct(productId, productRequest);

        // 查詢更新商品數據
        Product updatedProduct = productService.getProductById(productId);

        // 回傳ResponseEntity給前端，updatedProduct放在boby傳給前端
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);

    }

    // 刪除商品
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {

        // 不需要先查詢資料存不存在，前端只要該商品不在就好了
        productService.deleteProductById(productId);

        // 回傳204
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
