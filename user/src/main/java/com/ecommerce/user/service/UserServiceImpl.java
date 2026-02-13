package com.ecommerce.user.service;


import com.ecommerce.user.dto.AddressDto;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

//List<User> users = new ArrayList<>();
    @Override
    public List<UserResponse> getAllUser() {
        return userRepository.findAll().stream().map(this::mapToUserResponse)
                .collect(Collectors.toList());

    }



    @Override
    public Optional<UserResponse> getUser(String id) {

        return userRepository.findById(id)
                .map(this::mapToUserResponse);
    }

    @Override
    public void createUser(UserRequest userRequest) {
        User user =new User();
        updateUserFromRequest(user,userRequest);
         userRepository.save(user);
    }

    @Override
    public boolean updateUser(String id ,UserRequest userRequest) {
        return userRepository.findById(id)
                .map(existingUser ->{
                   updateUserFromRequest(existingUser , userRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);

    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        if(user != null)
        {

            userResponse.setId(user.getId());
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
            userResponse.setEmail(user.getEmail());
            userResponse.setPhone(user.getPhone());
            userResponse.setRole(user.getRole());
            if(user.getAddress() !=null)
            {
                Address address = user.getAddress();
                AddressDto addressDto = new AddressDto();
                addressDto.setStreet(address.getStreet());
                addressDto.setCity(address.getCity());
                addressDto.setState(address.getState());
                addressDto.setCountry(address.getCountry());
                addressDto.setZipcode(address.getZipcode());
                userResponse.setAddress(addressDto);
            }
        }
        return userResponse;

    }

    public void updateUserFromRequest(User user ,UserRequest userRequest)
    {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if(userRequest.getAddress() !=null)
        {
            AddressDto addressDto =userRequest.getAddress();
            Address address = new Address();
            address.setStreet(addressDto.getStreet());
            address.setCity(addressDto.getCity());
            address.setState(addressDto.getState());
            address.setCountry(addressDto.getCountry());
            address.setZipcode(addressDto.getZipcode());
            user.setAddress(address);
        }
    }
}
