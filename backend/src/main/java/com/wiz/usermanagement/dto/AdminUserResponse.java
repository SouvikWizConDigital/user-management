package com.wiz.usermanagement.dto;

import com.wiz.usermanagement.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AdminUserResponse extends UserResponse {
    private boolean deleted;
    private Instant deletedAt;

    public AdminUserResponse(User user) {
        super(user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber(), user.getRoles());
        this.deleted = user.isDeleted();
        this.deletedAt = user.getDeletedAt();
    }
}
