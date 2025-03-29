package com.tanniafashion.tanniafashion.services;

import java.util.List;

import com.tanniafashion.tanniafashion.models.Order;

public interface OrderService {
    Order checkout(Long userId);
    List<Order> getOrdersByUserId(Long userId);
    Order getOrderById(Long orderId);
    void updateOrderStatus(Long orderId, String status);
}
