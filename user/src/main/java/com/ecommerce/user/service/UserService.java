package com.ecommerce.user.service;



import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<UserResponse> getAllUser();

    public Optional<UserResponse> getUser(String id);

    public void createUser(UserRequest userRequest);

    boolean updateUser(String id , UserRequest userRequest);
}
