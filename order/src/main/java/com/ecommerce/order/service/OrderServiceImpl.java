package com.ecommerce.order.service;


import com.ecommerce.order.dto.OrderItemDto;
import com.ecommerce.order.dto.OrderRespons;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final CartItemService cartItemService;
    private final OrderRepository orderRepository;

    @Override
    public Optional<OrderRespons> createOrder(String userId) {

        //Validate Cart
        List<CartItem> cartItems = cartItemService.getCart(userId);
        if(cartItems.isEmpty())
            return Optional.empty();

        //Calculate Total price
        BigDecimal totalPrice = cartItems.stream()
                .map(cartItem -> cartItem.getPrice()
                        .multiply(new BigDecimal(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                )).toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        //Clear the Cart
        cartItemService.clearCart(userId);


        return Optional.of(mapToObjectResponse(savedOrder));
    }

    private OrderRespons mapToObjectResponse(Order order) {
        return new OrderRespons(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream().map(
                        orderItem -> new OrderItemDto(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                        )
                ).toList(),
                order.getCreatedAt()
        );
    }
}
