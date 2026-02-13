package com.ecommerce.user.controller;


import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    ResponseEntity<List<UserResponse>> getAllUser()
    {
        return new ResponseEntity<>(userService.getAllUser() , HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    ResponseEntity<UserResponse> getUser(@PathVariable("id") String id)
    {
        log.info("This is user INFO log");
        log.debug("This is user DEBUG log");
        log.trace("This is user TRACE log");
        log.warn("This is user WARN log");
        log.error("This is user ERROR log");
        Optional<UserResponse> user = userService.getUser(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }



    @PostMapping("/users")
    ResponseEntity<String> createUser(@RequestBody UserRequest userRequest)
    {
        // user.setId(idCount++);
        userService.createUser(userRequest);
        return ResponseEntity.ok("The user created successfully");
    }

    @PutMapping("/users/{id}")
    ResponseEntity<String> updateUser(@PathVariable String id , @RequestBody UserRequest userRequest)
    {

        boolean updated =userService.updateUser(id , userRequest);
        System.out.println("Update Calling with id " +id  +" : "+ updated);

        if(updated) {
            return ResponseEntity.ok("The user updated successfully");
        }

        return ResponseEntity.notFound().build();


    }

}
