package com.sigmadevs.tech.app.dto;

import com.sigmadevs.tech.security.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String displayName;
    private Boolean isEmailVerified;
    private String email;
    private String bio;
    private Role role;
    private String image;
}
