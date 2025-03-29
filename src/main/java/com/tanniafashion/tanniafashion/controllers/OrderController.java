package com.tanniafashion.tanniafashion.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanniafashion.tanniafashion.models.Cart;
import com.tanniafashion.tanniafashion.models.CartItem;
import com.tanniafashion.tanniafashion.models.Order;
import com.tanniafashion.tanniafashion.services.CartService;
import com.tanniafashion.tanniafashion.services.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<?> checkout(@PathVariable Long userId, @RequestBody Map<String, List<CartItem>> body) {
        List<CartItem> items = body.get("items");

        if(items == null || items.isEmpty()) {
            throw new IllegalStateException("El carrito esta vacio");
        }

        Cart cart = cartService.getCartByUserId(userId);

        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito esta vacio para el usuario: " + userId);
        }

        cart.getItems().clear();
        cart.getItems().addAll(items);
        cartService.updateCart(cart);
        Order order = orderService.checkout(userId);
        if (order == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(order);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) { 
        List<Order> orders = orderService.getOrdersByUserId(userId);
        if (orders.isEmpty()) { 
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }
}
