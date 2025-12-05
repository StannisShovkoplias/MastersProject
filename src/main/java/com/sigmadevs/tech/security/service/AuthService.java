package com.sigmadevs.tech.security.service;

import com.sigmadevs.tech.app.entity.User;
import com.sigmadevs.tech.app.service.EmailService;
import com.sigmadevs.tech.security.config.JwtUtil;
import com.sigmadevs.tech.security.dto.UserLoginDto;
import com.sigmadevs.tech.security.dto.UserRegistrationDto;
import com.sigmadevs.tech.security.entity.LoginToken;
import com.sigmadevs.tech.security.entity.Token;
import com.sigmadevs.tech.security.mapper.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final LoginTokenService confirmationTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.security.domain}")
    private String domain;

    @Value("${server.domain}")
    private String serverDomain;

    public ResponseEntity<String> login(UserLoginDto userLoginDto) {
        String login = userLoginDto.getLogin();
        User user = userService.findByUsername(login);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), userLoginDto.getPassword()));
            setCookie(user);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed");
        }
        return ResponseEntity.ok("Successfully logged in");
    }

    public ResponseEntity<String> registration(UserRegistrationDto userRegistrationDto) {
        User user = userService.save(userMapper.userRegistrationDtoToUser(userRegistrationDto));
        setCookie(user);
        return ResponseEntity.ok("User registered successfully");
    }

    private void setCookie(User user) {
        if (!domain.startsWith("http://") && !domain.startsWith("https://")) {
            throw new IllegalArgumentException("Invalid URL format: " + domain);
        }
        String pattern = domain.replaceFirst("^(https?://)", ".");
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes()))
                .getResponse();
        ResponseCookie responseCookie = ResponseCookie.from("accessToken", jwtUtil.generateToken(user)).
                httpOnly(true).
                maxAge(24_192_000).
                sameSite("None").
                domain(pattern).
                path("/").
                secure(true).
                build();
        Objects.requireNonNull(response).addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public ResponseEntity<String> confirmEmail(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (user.getIsEmailVerified()){
            return ResponseEntity.ok("Email verified");
        }
        LoginToken token = confirmationTokenService.generateConfirmationToken(user);
        String html = """
                <div>
                      <style>
                         html {
                            box-sizing: border-box;
                         }
                         *,
                         *::before,
                         *::after {
                            padding: 0;
                            margin: 0;
                            box-sizing: inherit;
                         }
                         .crad-wrapper {
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 350px;
                            width: 100%;
                            background: #111113;
                            font-family: sans-serif;
                            color: #edeef0;
                         }
                         .card {
                            border-radius: 8px;
                            border: 1px solid #363a3e;
                            background: #18191b;
                            padding: 12px;
                            display: flex;
                            flex-direction: column;
                            gap: 0.5rem;
                         }
                
                         .card-gui {
                            display: flex;
                            justify-content: center;
                            align-items: center;
                         }
                
                         .card-gui button {
                            background: #3c9eff;
                            color: #edeef0;
                            padding: 0.25rem 1rem;
                            border-radius: 4px;
                            border: 1px solid #1879db;
                            cursor: pointer;
                            font-size: 1.25rem;
                         }
                
                         .card-gui button:hover {
                            background: #1f7fdf;
                         }
                      </style>
                      <div class="crad-wrapper">
                         <div class="card">
                            <h1>Sabaody <b style="color: #3c9eff">Space</b></h1>
                            <p>Click this button to confirm your email</p>
                            <div class="card-gui">
                               <a href="urll">
                                  <button>Confirm email</button>
                               </a>
                            </div>
                         </div>
                      </div>
                   </div>
                """.replaceFirst("urll",serverDomain+"/confirm?token="+token.getToken());
        emailService.sendMessage(user.getEmail(), html,"Confirm email");
        return ResponseEntity.ok("Email sent successfully");
    }

    public ResponseEntity<String> confirm(String token) {
        confirmationTokenService.getToken(token).ifPresentOrElse(
                (confirmationToken)->{
                    if (!confirmationToken.getTokenType().equals(Token.CONFIRM)){
                        throw new BadCredentialsException("Token is not confirm");
                    }
                    if(confirmationToken.getExpiredAt().isAfter(LocalDateTime.now())){
                        User user = confirmationToken.getUser();
                        user.setIsEmailVerified(true);
                        userService.update(user);
                        confirmationTokenService.delete(confirmationToken);
                    }else {
                        throw new BadCredentialsException("Token expired");
                    }
                },()->{
                    throw new BadCredentialsException("Invalid token");
                }
        );
        return ResponseEntity.ok("Email confirmed");
    }

    public ResponseEntity<String> resetPassword(String email) {
        boolean b = userService.existsByEmail(email);
        if (!b){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email does not register");
        }
        User user = userService.findByEmailOptional(email).orElseThrow();
        LoginToken token = confirmationTokenService.generateResetToken(user);
        String html = """
                <div>
                      <style>
                         html {
                            box-sizing: border-box;
                         }
                         *,
                         *::before,
                         *::after {
                            padding: 0;
                            margin: 0;
                            box-sizing: inherit;
                         }
                         .crad-wrapper {
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 350px;
                            width: 100%;
                            background: #111113;
                            font-family: sans-serif;
                            color: #edeef0;
                         }
                         .card {
                            border-radius: 8px;
                            border: 1px solid #363a3e;
                            background: #18191b;
                            padding: 12px;
                            display: flex;
                            flex-direction: column;
                            gap: 0.5rem;
                         }
                
                         .card-gui {
                            display: flex;
                            justify-content: center;
                            align-items: center;
                         }
                
                         .card-gui button {
                            background: #3c9eff;
                            color: #edeef0;
                            padding: 0.25rem 1rem;
                            border-radius: 4px;
                            border: 1px solid #1879db;
                            cursor: pointer;
                            font-size: 1.25rem;
                         }
                
                         .card-gui button:hover {
                            background: #1f7fdf;
                         }
                      </style>
                      <div class="crad-wrapper">
                         <div class="card">
                            <h1>Sabaody <b style="color: #3c9eff">Space</b></h1>
                            <p>Click this button to reset your password</p>
                            <div class="card-gui">
                               <a href="urll">
                                  <button>Reset password</button>
                               </a>
                            </div>
                         </div>
                      </div>
                   </div>
                """.replaceFirst("urll",domain+"/resetPassword?token="+token.getToken());
        emailService.sendMessage(user.getEmail(), html,"Reset password");
        return ResponseEntity.ok("Reset password sent successfully");
    }

    public ResponseEntity<String> reset(String token) {
        AtomicReference<String> newPassword = new AtomicReference<>();
        confirmationTokenService.getToken(token).ifPresentOrElse(
                (confirmationToken)->{
                    if (!confirmationToken.getTokenType().equals(Token.RESET)){
                        throw new BadCredentialsException("Token is not reset");
                    }
                    if(confirmationToken.getExpiredAt().isAfter(LocalDateTime.now())){
                        String substring = UUID.randomUUID().toString().substring(0, 10);
                        String password = passwordEncoder.encode(substring);
                        User user = confirmationToken.getUser();
                        user.setPassword(password);
                        userService.update(user);
                        confirmationTokenService.delete(confirmationToken);
                        newPassword.set(substring);

                    }else {
                        throw new BadCredentialsException("Token expired");
                    }
                },()->{
                    throw new BadCredentialsException("Invalid token");
                }
        );
        return ResponseEntity.ok(newPassword.get());
    }
}