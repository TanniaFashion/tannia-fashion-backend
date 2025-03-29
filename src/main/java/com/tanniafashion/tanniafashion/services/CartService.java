package com.tanniafashion.tanniafashion.services;

import com.tanniafashion.tanniafashion.models.Cart;
import com.tanniafashion.tanniafashion.models.CartItem;

public interface CartService {
    Cart getCartByUserId(Long userId);
    Cart addToCart(Long userId, CartItem item);
    void clearCart(Long userId);
    Cart removeItem(Long userId, Long productId);
    Cart updateCart(Cart cart);
}
