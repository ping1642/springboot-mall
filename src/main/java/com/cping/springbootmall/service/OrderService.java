package com.cping.springbootmall.service;

import com.cping.springbootmall.dto.CreateOrderRequest;
import com.cping.springbootmall.model.Order;

public interface OrderService {

    // 查詢訂單資訊
    Order getOrderById(Integer orderId);

    // 創建訂單
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);


}
