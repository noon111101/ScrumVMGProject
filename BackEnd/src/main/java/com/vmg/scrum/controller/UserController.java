package com.vmg.scrum.controller;

import com.vmg.scrum.entity.User;
import com.vmg.scrum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth/user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @GetMapping("")
    public List<User> getUsers(){
        return userRepository.findAll();
    }


}
