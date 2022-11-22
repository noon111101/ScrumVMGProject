package com.vmg.scrum.service;

import com.vmg.scrum.model.User;
import com.vmg.scrum.payload.request.ChangePasswordRequest;
import com.vmg.scrum.payload.request.LoginRequest;
import com.vmg.scrum.payload.request.SignupRequest;
import com.vmg.scrum.payload.request.UpdateUserRequest;
import com.vmg.scrum.payload.response.JwtResponse;
import com.vmg.scrum.payload.response.MessageResponse;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


public interface UserService   {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    MessageResponse registerUser(SignupRequest signUpRequest) throws MessagingException, UnsupportedEncodingException;

    MessageResponse updatePassword(ChangePasswordRequest changePasswordRequest);

    MessageResponse lockAccount(Long id);


    void updateUser(long id, UpdateUserRequest updateRequest);

}
