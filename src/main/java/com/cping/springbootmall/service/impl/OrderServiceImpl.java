package com.cping.springbootmall.service.impl;

import com.cping.springbootmall.dao.OrderDao;
import com.cping.springbootmall.dao.ProductDao;
import com.cping.springbootmall.dto.BuyItem;
import com.cping.springbootmall.dto.CreateOrderRequest;
import com.cping.springbootmall.model.Order;
import com.cping.springbootmall.model.OrderItem;
import com.cping.springbootmall.model.Product;
import com.cping.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    // 查詢訂單資訊
    @Override
    public Order getOrderById(Integer orderId) {
        // 取得order table的數據
        Order order = orderDao.getOrderById(orderId);

        // 取得orderItem table的數據
        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemList);

        return order;
    }

    // 創建訂單
    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {

        // 商品總價格
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        // for loop 使用者所購買的每一個商品
        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            // 使用productId查詢product數據
            Product product = productDao.getProductById(buyItem.getProductId());

            // 計算總價格
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount = totalAmount + amount;

            // 轉換 BuyItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        // 創建訂單，兩張table
        // 訂單總資訊
        Integer orderId = orderDao.createOrder(userId, totalAmount);

        // 訂單詳情記錄
        orderDao.createOrderItems(orderId, orderItemList);

        return  orderId;

    }
}
