package com.wiz.usermanagement.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String name;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", length = 10)
    @Pattern(regexp = "^[0-9]{10}$")
    private String phoneNumber;

}
