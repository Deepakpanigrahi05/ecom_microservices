package com.ecommerce.order.controller;


import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId
            , @RequestBody CartItemRequest cartItemRequest){

       boolean isCreated = cartItemService.addToCart(userId , cartItemRequest);
       if(isCreated)
           return ResponseEntity.status(HttpStatus.CREATED).build();

       return ResponseEntity.badRequest().body("Product out of stock or User not found or Product not found");
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> deleteCart(@RequestHeader("X-User-ID") String id
            , @PathVariable String productId){

        boolean isdeleted = cartItemService.deleteCart(id , productId);
      return isdeleted ? ResponseEntity.noContent().build()
              : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(cartItemService.getCart(userId));
    }


}
