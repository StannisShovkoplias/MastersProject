package com.sigmadevs.tech.security.api;

import com.sigmadevs.tech.app.dto.UserDto;
import com.sigmadevs.tech.security.dto.UserInfo;
import com.sigmadevs.tech.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount(Principal principal) {
        if(userService.deleteByUsername(principal.getName())){
            return ResponseEntity.ok("Account deleted successfully");
        }else {
            return ResponseEntity.badRequest().body("Account could not be deleted");
        }
    }

    @PatchMapping(value = "/updateImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> updateImage(Principal principal, @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(userService.updateImage(image,principal));
    }

    @PatchMapping(value = "/updatePassword")
    public ResponseEntity<UserDto> updatePassword(Principal principal, @RequestBody String password) {
        return ResponseEntity.ok(userService.updatePassword(principal,password));
    }

    @PatchMapping("/updateUsername")
    public ResponseEntity<String> updateUsername(Principal principal, @RequestBody String username) {
        if (username == null || username.length() < 3 || username.length() > 20) {
            return ResponseEntity.badRequest().body("Username must be between 6 and 30 characters");
        }
        return userService.updateUsername(principal,username);
    }

    @PutMapping("/updateInfo")
    public ResponseEntity<UserDto> updateInfo(Principal principal, @RequestBody UserInfo info) {
        return userService.updateInfo(principal,info);
    }

    @PatchMapping("/updateEmail")
    public ResponseEntity<String> updateEmail(Principal principal, @RequestBody String email) {
        return userService.updateEmail(principal,email);
    }


}
