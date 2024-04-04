package com.cping.springbootmall.controller;


import com.cping.springbootmall.dto.CreateOrderRequest;
import com.cping.springbootmall.model.Order;
import com.cping.springbootmall.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;


    // 創建訂單
    // 使用者一定要先有帳號，才能在帳號底下去購買東西
    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest) {

        // 創建訂單
        Integer orderId = orderService.createOrder(userId, createOrderRequest);

        // 整筆訂單資訊回給前端
        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

}
