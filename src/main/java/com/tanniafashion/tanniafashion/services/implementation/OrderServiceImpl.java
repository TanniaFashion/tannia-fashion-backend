package com.tanniafashion.tanniafashion.services.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tanniafashion.tanniafashion.models.Cart;
import com.tanniafashion.tanniafashion.models.CartItem;
import com.tanniafashion.tanniafashion.models.Order;
import com.tanniafashion.tanniafashion.models.OrderDetail;
import com.tanniafashion.tanniafashion.models.Product;
import com.tanniafashion.tanniafashion.repositories.CartRepository;
import com.tanniafashion.tanniafashion.repositories.OrderDetailRepository;
import com.tanniafashion.tanniafashion.repositories.OrderRepository;
import com.tanniafashion.tanniafashion.repositories.ProductRepository;
import com.tanniafashion.tanniafashion.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Order checkout(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalStateException("Carrito no encontrado"));

        System.out.println("DEBUG: Carrito recuperado para usuario ID: " + userId);
        System.out.println("Items en el carrito: " + cart.getItems().size());

        if (cart.getItems().isEmpty()) {
            System.out.println("DEBUG: El carrito está vacío para el usuario ID: " + userId);
            throw new IllegalStateException("El carrito esta vacío");
        }

        for (CartItem item : cart.getItems()) { 
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (item.getQuantity() > product.getStock()) { 
                throw new IllegalStateException("Stock insuficiente para el producto: " + product.getName());
            }

            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus("Pendiente");

        Order savedOrder = orderRepository.save(order);

        cart.getItems().forEach(item ->{
            OrderDetail detail = new OrderDetail();
                detail.setOrder(savedOrder);
                detail.setProductId(item.getProductId());
                detail.setProductName(item.getProductName());
                detail.setQuantity(item.getQuantity());
                detail.setPricePerUnit(item.getPricePerUnit());
                orderDetailRepository.save(detail);
        });

        return savedOrder;
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) { 
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        if ("Pagado".equals(status)) { 
            for (OrderDetail detail : order.getDetails()) { 
                Product product = productRepository.findById(detail.getProductId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                product.setStock(product.getStock() - detail.getQuantity());
                productRepository.save(product);
            }
        }

        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));
    }
    
}
