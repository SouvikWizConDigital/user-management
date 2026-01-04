package com.wiz.usermanagement.service;

import com.wiz.usermanagement.dto.UserRequest;
import com.wiz.usermanagement.dto.UserResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse addUser(UserRequest user);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(@NotNull UUID userId);

    UserResponse updateUser(@NotNull UUID userId, UserRequest user);

    void deleteUser(@NotNull UUID userId);

    void restoreUser(@NotNull UUID userId);

    List<UserResponse> getAllDeletedUsers();
}
