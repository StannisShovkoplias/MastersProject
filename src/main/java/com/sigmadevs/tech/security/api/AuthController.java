package com.sigmadevs.tech.security.api;

import com.sigmadevs.tech.app.dto.UserDto;
import com.sigmadevs.tech.security.dto.UserGetDto;
import com.sigmadevs.tech.security.dto.UserLoginDto;
import com.sigmadevs.tech.security.dto.UserRegistrationDto;
import com.sigmadevs.tech.security.mapper.UserMapper;
import com.sigmadevs.tech.security.service.AuthService;
import com.sigmadevs.tech.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        return authService.login(userLoginDto);
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<String> registration(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        return authService.registration(userRegistrationDto);
    }

    @GetMapping("/currentAccount")
    public ResponseEntity<UserDto> getCurrentAccount(Principal principal) {
        return ResponseEntity.ok(userMapper.userToUserDto(userService.findByUsername(principal.getName())));
    }

    @PostMapping("/confirmEmail")
    public ResponseEntity<String> confirmEmail(Principal principal) {
        return authService.confirmEmail(principal);
    }
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        return authService.confirm(token);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody String email) {
        return authService.resetPassword(email);
    }

    @GetMapping("/reset")
    public ResponseEntity<String> reset(@RequestParam("token") String token) {
        return authService.reset(token);
    }

}


