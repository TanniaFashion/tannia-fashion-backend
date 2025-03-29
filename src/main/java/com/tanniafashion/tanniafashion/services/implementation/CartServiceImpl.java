package com.tanniafashion.tanniafashion.services.implementation;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tanniafashion.tanniafashion.models.Cart;
import com.tanniafashion.tanniafashion.models.CartItem;
import com.tanniafashion.tanniafashion.models.Product;
import com.tanniafashion.tanniafashion.repositories.CartRepository;
import com.tanniafashion.tanniafashion.repositories.ProductRepository;
import com.tanniafashion.tanniafashion.services.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return cartRepository.save(newCart);
        });
    }

    @Override
    public Cart addToCart(Long userId, CartItem item) {
        Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (item.getQuantity() > product.getStock()) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + product.getName());
        }

        Cart cart = getCartByUserId(userId);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(item.getProductId()))
                .findFirst();
        
        if (existingItem.isPresent()) {
            int newQuantity = existingItem.get().getQuantity() + item.getQuantity();
            if (newQuantity > product.getStock()) {
                throw new IllegalStateException("Stock insuficiente al agregar mas unidades de: " + product.getName());
            }

            if (newQuantity <= 0) {
                cart.getItems().remove(existingItem.get());
            } else { 
                existingItem.get().setQuantity(newQuantity);
            }
        } else {
            if (item.getQuantity() > 0) {
                item.setProductName(product.getName());
                item.setPricePerUnit(product.getPrice());
                item.setImgUrl(product.getImgUrl());

                item.setCart(cart);
                cart.getItems().add(item);
            }
        }

        cart.calculateTotalPrice();

        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long userId) {
        cartRepository.findByUserId(userId).ifPresent(cart -> {
            cart.getItems().clear();
            cart.setTotalPrice(BigDecimal.ZERO);
            cartRepository.save(cart);
        });
    }

    @Override
    public Cart removeItem(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart == null) return null;

        cart.getItems().removeIf(item -> item.getProductId().equals(productId));

        cart.calculateTotalPrice();

        return cartRepository.save(cart);
    }

    @Override
    public Cart updateCart(Cart cart) {
        return cartRepository.save(cart);
    }
    
}
