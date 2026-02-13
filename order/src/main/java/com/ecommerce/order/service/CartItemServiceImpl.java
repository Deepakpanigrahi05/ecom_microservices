package com.ecommerce.order.service;


import com.ecommerce.order.config.ProductServiceClient;
import com.ecommerce.order.config.UserServiceClient;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemServiceImpl implements CartItemService{

    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;


    @Override
    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {

       ProductResponse productResponse = productServiceClient.getProductDetails(cartItemRequest.getProductId());
        if(productResponse == null || productResponse.getStockQuantity() < cartItemRequest.getQuantity())
        {
            return false;
        }

        UserResponse userResponse = userServiceClient.getUserDetails(userId);
        if(userResponse == null)
            return false;
//
//        User user = userOptional.get();
        CartItem existingCartItem =
                cartItemRepository.findByUserIdAndProductId(userId, cartItemRequest.getProductId());
        if(existingCartItem !=null)
        {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(existingCartItem);
        }else{
            CartItem newCartItem = new CartItem();
            newCartItem.setUserId(userId);
            newCartItem.setProductId(cartItemRequest.getProductId());
            newCartItem.setQuantity(cartItemRequest.getQuantity());
            newCartItem.setPrice(BigDecimal.valueOf(1000));

            cartItemRepository.save(newCartItem);
        }


        return true;
    }

    @Override
    public boolean deleteCart(String userId, String productId) {

    CartItem cartItem =    cartItemRepository.findByUserIdAndProductId(userId , productId);
         if(cartItem != null)
         {
             cartItemRepository.delete(cartItem);
             return true;
         }
        return false;
    }

    @Override
    public List<CartItem> getCart(String userId) {

        return cartItemRepository.findByUserId(userId);
    }

    @Override
    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);

    }
}
