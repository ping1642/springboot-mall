package com.cping.springbootmall.service.impl;

import com.cping.springbootmall.dao.OrderDao;
import com.cping.springbootmall.dao.ProductDao;
import com.cping.springbootmall.dao.UserDao;
import com.cping.springbootmall.dto.BuyItem;
import com.cping.springbootmall.dto.CreateOrderRequest;
import com.cping.springbootmall.model.Order;
import com.cping.springbootmall.model.OrderItem;
import com.cping.springbootmall.model.Product;
import com.cping.springbootmall.model.User;
import com.cping.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

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

        // 檢查 user 是否存在
        User user = userDao.getUserById(userId);

        if (user == null) {
            log.warn("該 userId: {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


        // 商品總價格
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        // for loop 使用者所購買的每一個商品
        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            // 使用productId查詢product數據
            Product product = productDao.getProductById(buyItem.getProductId());

            // 檢查 product 是否存在、庫存是否足夠
            if (product == null) {
                log.warn("商品: {} 不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock() < buyItem.getQuantity()) {
                log.warn("商品: {} 庫存數量不足，無法購買。剩餘庫存: {}，欲購買數量: {}",
                        buyItem.getProductId(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 扣除商品庫存
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());


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
