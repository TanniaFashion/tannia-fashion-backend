package com.tanniafashion.tanniafashion.controllers;

import java.math.BigDecimal;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanniafashion.tanniafashion.models.Cart;
import com.tanniafashion.tanniafashion.models.CartItem;
import com.tanniafashion.tanniafashion.models.Product;
import com.tanniafashion.tanniafashion.repositories.ProductRepository;
import com.tanniafashion.tanniafashion.services.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) { 
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            Cart emptyCart = new Cart();
            emptyCart.setUserId(userId);
            emptyCart.setItems(new HashSet<>());
            emptyCart.setTotalPrice(BigDecimal.ZERO);
            return ResponseEntity.ok(emptyCart);
        }
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<Cart> addToCart(@PathVariable Long userId, @RequestBody CartItem item) {
        if (item.getProductId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Product product = productRepository.findById(item.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        item.setProductName(product.getName());
        item.setPricePerUnit(product.getPrice());

        Cart updatedCart = cartService.addToCart(userId, item);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/remove/{userId}/{productId}")
    public ResponseEntity<Cart> removeItem(@PathVariable Long userId, @PathVariable Long productId) {
        Cart updatedCart = cartService.removeItem(userId, productId);
        return ResponseEntity.ok(updatedCart);
    }
}
