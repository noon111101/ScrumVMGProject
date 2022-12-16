package com.vmg.scrum.controller;

import com.vmg.scrum.model.User;


import com.vmg.scrum.model.excel.LogDetail;

import com.vmg.scrum.payload.request.ManageUser_Request;
import com.vmg.scrum.payload.request.SignupRequest;
import com.vmg.scrum.payload.request.UpdateUserRequest;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-management")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    // Hiển thị User (theo phòng ban, search, trạng thái)
    @GetMapping("users")
    public ResponseEntity<Page<User>> getUsers(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @ModelAttribute ManageUser_Request manageUser_request){
        Pageable pageable = PageRequest.of(page,size);
        Page<User> pageUsers = userService.manageUsers(manageUser_request, pageable);

        return new ResponseEntity<>(pageUsers, HttpStatus.OK);
    }

    @GetMapping("users/{id}")
    public Optional<User> getUsers(@PathVariable Long id){
        return userRepository.findById(id);
    }
    @GetMapping("user/id_staff")
    public User getUser(@RequestParam String code){
        return userRepository.findByCode(code);
    }
    @GetMapping("users/page")
    public ResponseEntity<Page<User>> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "2") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(userRepository.findAll(pageable), HttpStatus.OK);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateTodo(@PathVariable("id") long id,@ModelAttribute UpdateUserRequest updateRequest) {
        userService.updateUser(id, updateRequest);
        return new ResponseEntity<>(userRepository.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<User>> getUser(){
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/getAllUserByDepartmentId/{id}")
    public ResponseEntity<List<User>> getUserByDepartmentId(@PathVariable("id") long id,@ModelAttribute UpdateUserRequest updateRequest){
        return new ResponseEntity<>(userRepository.findAllByDepartments_Id(id), HttpStatus.OK);
    }


    @PostMapping("create")
    public MessageResponse registerUserExcel(@RequestBody SignupRequest[] signupRequests){
        try {
            for(SignupRequest s : signupRequests){
                userService.registerUserPasswordDefault(s);
            }
            return new MessageResponse("Thêm người dùng thành công");
        } catch (Exception e) {
            throw new RuntimeException("Đăng kí lỗi trường thông tin chưa đúng quy định");
        }
    }
}
