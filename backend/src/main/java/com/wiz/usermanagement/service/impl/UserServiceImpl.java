package com.wiz.usermanagement.service.impl;

import com.wiz.usermanagement.config.persistence.HibernateFilterManager;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HibernateFilterManager filterManager;

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
        return toResponse(savedUser);
    }


    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return toResponse(user);
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
        return toResponse(updatedUser);
    }

    @Override
    public void deleteUser(Integer userId) {

        filterManager.enableNotDeletedFilter();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id: " + userId));

        userRepository.delete(user);
    }

    @Override
    public void restoreUser(Integer userId) {

        filterManager.disableDeletedFilter();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Deleted user not found with id: " + userId));

        user.setDeleted(false);
        user.setDeletedAt(null);

        userRepository.save(user);

        filterManager.enableNotDeletedFilter();
    }

    @Override
    public List<UserResponse> getAllDeletedUsers() {

        filterManager.enableDeletedFilter();

        return userRepository.findAllDeletedUsers()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }


}
