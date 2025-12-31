package com.wiz.usermanagement.service;

import com.wiz.usermanagement.dto.UserRequest;
import com.wiz.usermanagement.dto.UserResponse;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface UserService {

    UserResponse addUser(UserRequest user);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(@Positive Integer userId);

    UserResponse updateUser(@Positive Integer userId, UserRequest user);

    void deleteUser(@Positive Integer userId);
}
