package com.wiz.usermanagement.service;

import com.wiz.usermanagement.model.User;
import org.springframework.stereotype.Service;

public interface UserService {
    User addUser(User user);
}
