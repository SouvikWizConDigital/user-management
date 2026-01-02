package com.wiz.usermanagement.service.impl;

import com.wiz.usermanagement.dto.UserRequest;
import com.wiz.usermanagement.dto.UserResponse;
import com.wiz.usermanagement.exception.EmailAlreadyExistsException;
import com.wiz.usermanagement.exception.UserNotFoundException;
import com.wiz.usermanagement.model.User;
import com.wiz.usermanagement.repository.UserRepository;
import com.wiz.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse addUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .build();

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getPhoneNumber());
    }


    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhoneNumber()
                )).toList();
    }

    @Override
    public UserResponse getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }

    @Override
    public UserResponse updateUser(Integer userId, UserRequest userRequest) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id: " + userId));

        User user = User.builder()
                .id(existingUser.getId())
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .build();

        User updatedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(updatedUser.getId());
        response.setName(updatedUser.getName());
        response.setEmail(updatedUser.getEmail());
        response.setPhoneNumber(updatedUser.getPhoneNumber());

        return response;
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id: " + userId));

        userRepository.delete(user);
    }


}
