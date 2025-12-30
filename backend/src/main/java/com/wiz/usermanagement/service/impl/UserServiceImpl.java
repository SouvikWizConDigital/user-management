package com.wiz.usermanagement.service.impl;

import com.wiz.usermanagement.exception.EmailAlreadyExistsException;
import com.wiz.usermanagement.model.User;
import com.wiz.usermanagement.repository.UserRepository;
import com.wiz.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;

    @Autowired // for 1 constructor it is optional
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }
        return userRepository.save(user);
    }
}
