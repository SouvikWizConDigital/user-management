package com.wiz.usermanagement.controller;

import com.wiz.usermanagement.dto.UserResponse;
import com.wiz.usermanagement.model.User;
import com.wiz.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    public final UserService userService;

    @Autowired // for 1 constructor it is optional
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/adduser") // Best practice: use plural nouns
    public ResponseEntity<UserResponse> addUser(@RequestBody User user) {
        User savedUser = userService.addUser(user);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
