package com.cping.springbootmall.dao;

import com.cping.springbootmall.dto.OrderQueryParams;
import com.cping.springbootmall.model.Order;
import com.cping.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    // 取得 order list
    List<Order> getOrders(OrderQueryParams orderQueryParams);

    // 取得 order 總數
    Integer countOrder(OrderQueryParams orderQueryParams);

    // 取得order table的數據
    Order getOrderById(Integer orderId);

    // 取得orderItem table的數據
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    // 訂單總資訊
    Integer createOrder(Integer userId, Integer totalAmount);

    // 訂單詳情記錄
    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

}
