package com.cping.springbootmall.controller;

import com.cping.springbootmall.constant.ProductCategory;
import com.cping.springbootmall.dto.ProductQueryParams;
import com.cping.springbootmall.dto.ProductRequest;
import com.cping.springbootmall.model.Product;
import com.cping.springbootmall.service.ProductService;
import com.cping.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 因使用了@Max、@Min，所以須加「@Validated」
@Validated
@RestController
public class ProductController {

    // 注入ProductService
    @Autowired
    private ProductService productService;

    // 查詢商品列表
    // 即使商品數據不存在，但「/products」這個API是存在的，所以還是會回200給前端
    @GetMapping("/products")
    // 改用Page類型的Product數據
    public ResponseEntity<Page<Product>> getProducts(
            // 查詢條件 Filtering
            // 依category條件去查詢
            // @RequestParam 表示從url中取得請求參數
            // Spring Boot會自動將前端傳過來的字串去轉換成「ProductCategory」這個Enum
            // category應該為「可選」參數，而不是「必選」參數
            // 加入「required = false」，category變為「可選」參數
            @RequestParam(required = false) ProductCategory category,
            // 依關鍵字條件去查詢
            @RequestParam(required = false) String search,

            // 排序 Sorting
            // 排序(orderBy：根據什麼欄位排序，sort：升冪或降冪排序)
            // 預設值為created_date，根據這欄位排序
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,

            // 分頁 Pagination
            // limit：這次要取得幾筆商品數據，offset：跳過多少筆商品數據(offset 2:表示跳過2筆數據，第三筆開始)
            // @Max(1000):前端傳過來的參數值最大不能超過1000 @Min(0):不能傳負數
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ) {

        // 將前端的參數值存在productQueryParams
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        // 取得 product list
        // 依category條件去查詢
        // 依關鍵字條件去查詢
        // 一併將參數值傳出去
        List<Product> productList = productService.getProducts(productQueryParams);

        // 計算商品總筆數，會根據查詢條件而不同
        Integer total = productService.countProduct(productQueryParams);

        // 分頁
        // 將前端的值存到page內，再將由page回傳給前端
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        // 將查詢的商品數據放到results內，然後回傳給前端
        page.setResults(productList);



        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

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
