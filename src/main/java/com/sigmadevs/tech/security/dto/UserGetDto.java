package com.sigmadevs.tech.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGetDto {
    private Long id;
    private String email;
    private String username;
    private String image;
}
