package com.wiz.usermanagement.controller;

import com.wiz.usermanagement.dto.UserRequest;
import com.wiz.usermanagement.dto.UserResponse;
import com.wiz.usermanagement.model.User;
import com.wiz.usermanagement.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Validated
public class UserController {

    public final UserService userService;

    @Autowired // for 1 constructor it is optional
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/adduser") // Best practice: use plural nouns
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody UserRequest user) {
        UserResponse response = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/getallusers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> response = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/getuserbyid/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") @Positive Integer userId) {
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/updateuser/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable @Positive Integer userId,
            @Valid @RequestBody UserRequest userRequest) {
        UserResponse response = userService.updateUser(userId, userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/deleteuser/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


}
