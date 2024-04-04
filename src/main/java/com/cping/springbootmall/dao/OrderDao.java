package com.cping.springbootmall.dao;

import com.cping.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    // 訂單總資訊
    Integer createOrder(Integer userId, Integer totalAmount);

    // 訂單詳情記錄
    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

}
