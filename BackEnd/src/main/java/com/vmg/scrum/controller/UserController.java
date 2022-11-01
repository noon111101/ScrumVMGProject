package com.vmg.scrum.controller;

import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.payload.request.UpdateUserRequest;
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
    public ResponseEntity<Page<User>> getUsers(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
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

//    @PutMapping("users/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
//        Optional<User> userData = userRepository.findById(id);
//        if (userData.isPresent()) {
//            User users = userData.get();
//            users.setCode(user.getCode());
//            users.setFullName(user.getFullName());
//            users.setDepartments(user.getDepartments());
//            users.setRoles(user.getRoles());
//            return new ResponseEntity<>(userRepository.save(users), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateTodo(@PathVariable("id") long id,@ModelAttribute UpdateUserRequest updateRequest) {
        userService.updateUser(id, updateRequest);
        return new ResponseEntity<>(userRepository.findById(id).get(), HttpStatus.OK);
    }
}
