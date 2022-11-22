package com.vmg.scrum.controller;

import com.vmg.scrum.payload.request.ChangePasswordRequest;
import com.vmg.scrum.payload.request.ForgotPasswordChangeRequest;
import com.vmg.scrum.payload.request.LoginRequest;
import com.vmg.scrum.payload.request.SignupRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.UserRepository;
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

    UserRepository userRepository;

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
    public MessageResponse changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest)  {
        return userService.updatePassword(changePasswordRequest);
    }
        @PutMapping("/lockAccount/{id}")
    public MessageResponse lockAccount(@PathVariable Long id){
        return  userService.lockAccount(id);
    }

    @PostMapping("/reset_password-tokenLink")
    public MessageResponse changePassword(@Valid @RequestBody ForgotPasswordChangeRequest forgotPasswordChangeRequest)  {
        return userService.forgotPasswordChangeRequest(forgotPasswordChangeRequest);
    }
    @GetMapping ("/checkResetPassword")
    public boolean checkvalidateJwtEmail(@RequestParam String token)  {
        return userService.checkValidateJWTEmail(token);
    }}