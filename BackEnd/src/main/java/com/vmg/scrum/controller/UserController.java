package com.vmg.scrum.controller;

import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-management")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @GetMapping("users")
    public List<User> getUsers(){
        return userRepository.findAll();
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
    @PutMapping("users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        Optional<User> userData = userRepository.findById(id);
        if (userData.isPresent()) {
            User users = userData.get();
            users.setCode(user.getCode());
            users.setFullName(user.getFullName());
            users.setDepartments(user.getDepartments());
            users.setCover(user.getCover());
            users.setRoles(user.getRoles());
            return new ResponseEntity<>(userRepository.save(users), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
