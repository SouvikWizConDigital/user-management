package com.wiz.usermanagement.service.impl;

import com.wiz.usermanagement.dto.AdminUserResponse;
import com.wiz.usermanagement.dto.UserRequest;
import com.wiz.usermanagement.dto.UserResponse;
import com.wiz.usermanagement.exception.EmailAlreadyExistsException;
import com.wiz.usermanagement.exception.UserAlreadyRestoredException;
import com.wiz.usermanagement.exception.UserNotFoundException;
import com.wiz.usermanagement.model.User;
import com.wiz.usermanagement.model.enums.Role;
import com.wiz.usermanagement.repository.UserRepository;
import com.wiz.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse addUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        if (!isAdminUser() && request.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException("Only ADMINs can create ADMIN user");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .roles(request.getRoles())
                .build();

        return toResponse(userRepository.save(user));
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID userId) {
        return userRepository.findActiveById(userId)
                .map(this::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    @Override
    public UserResponse updateUser(UUID userId, UserRequest userRequest) {
        User existingUser = userRepository.findActiveById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        User user = User.builder()
                .id(existingUser.getId())
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .roles(userRequest.getRoles())
                .build();

        return toResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = userRepository.findActiveById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userRepository.delete(user);
    }

    @Override
    public void restoreUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (!user.isDeleted()) {
            throw new UserAlreadyRestoredException("User already restored with id: " + userId);
        }

        user.setDeleted(false);
        user.setDeletedAt(null);

        userRepository.save(user);
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllDeletedUsers() {
        return userRepository.findAllDeletedUsers()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private UserResponse toResponse(User user) {
        if (isAdminUser()) {
            return toAdminUserResponse(user);
        }
        return toUserResponse(user);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRoles()
        );
    }

    private AdminUserResponse toAdminUserResponse(User user) {
        return new AdminUserResponse(user);
    }

    private boolean isAdminUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }


}
