package com.vmg.scrum.service;

import com.vmg.scrum.entity.User;
import com.vmg.scrum.payload.request.LoginRequest;
import com.vmg.scrum.payload.request.SignupRequest;
import com.vmg.scrum.payload.response.JwtResponse;
import com.vmg.scrum.payload.response.MessageResponse;


public interface UserService  {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    MessageResponse registerUser(SignupRequest signUpRequest);

    void updatePassword(User user, String newPassword);

}
