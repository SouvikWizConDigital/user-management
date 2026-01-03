package com.wiz.usermanagement.controller;

import com.wiz.usermanagement.dto.UserRequest;
import com.wiz.usermanagement.dto.UserResponse;
import com.wiz.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@Tag(name = "User-Management")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
})
public class UserController {

    public final UserService userService;

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
    public ResponseEntity<UserResponse> updateUser(@PathVariable @Positive Integer userId, @Valid @RequestBody UserRequest userRequest) {
        UserResponse response = userService.updateUser(userId, userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/deleteuser/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/restoreuser/{userId}")
    public ResponseEntity<Void> restoreUser(@PathVariable @Positive Integer userId) {
        userService.restoreUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getdeletedusers")
    public ResponseEntity<List<UserResponse>> getDeletedUsers() {
        List<UserResponse> response = userService.getAllDeletedUsers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
