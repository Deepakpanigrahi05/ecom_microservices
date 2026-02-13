package com.ecommerce.order.service;


import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.dto.CartItemRequest;

import java.util.List;

public interface CartItemService {
    boolean addToCart(String userId, CartItemRequest cartItemRequest);

    boolean deleteCart(String id, String productId);

    List<CartItem> getCart(String userId);

    void clearCart(String userId);
}
