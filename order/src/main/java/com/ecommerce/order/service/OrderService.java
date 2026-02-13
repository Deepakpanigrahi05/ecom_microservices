package com.ecommerce.order.service;


import com.ecommerce.order.dto.OrderRespons;

import java.util.Optional;

public interface OrderService {
    Optional<OrderRespons> createOrder(String userId);
}
