package com.vmg.scrum.controller;

import com.vmg.scrum.payload.request.ChangePasswordRequest;
import com.vmg.scrum.payload.request.LoginRequest;
import com.vmg.scrum.payload.request.SignupRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @ModelAttribute SignupRequest signUpRequest) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.ok(userService.registerUser(signUpRequest));
    }

    @PostMapping("/changePassword")
    public Boolean changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest)  {
        return userService.updatePassword(changePasswordRequest);
    }
    @PutMapping("/lockAccount/{id}")
    public MessageResponse lockAccount(@PathVariable Long id , @RequestParam boolean lock){
        return  userService.lockAccount(id,lock);
    }
}