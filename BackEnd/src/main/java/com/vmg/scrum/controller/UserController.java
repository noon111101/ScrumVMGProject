package com.vmg.scrum.controller;

import com.vmg.scrum.model.Role;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.request.SignupRequest;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-management")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserService userService;
    @GetMapping("users")
    public ResponseEntity<Page<User>> getUsers(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size,
                                               @RequestParam(name = "departid", required = false) Long departid){
        Pageable pageable = PageRequest.of(page,size);
        Page<User> pageUsers = null;
        if(departid!=null && departid!=0){
            pageUsers = userRepository.getUsersByDepartments_Id(departid, pageable);
        }
        else{
            pageUsers = userRepository.findAll(pageable);
        }
        return new ResponseEntity<>(pageUsers, HttpStatus.OK);
    }
    @GetMapping("users/{id}")
    public Optional<User> getUsers(@PathVariable Long id){
        return userRepository.findById(id);
    }
    @GetMapping("users/page")
    public ResponseEntity<Page<User>> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "2") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(userRepository.findAll(pageable), HttpStatus.OK);
    }

}
