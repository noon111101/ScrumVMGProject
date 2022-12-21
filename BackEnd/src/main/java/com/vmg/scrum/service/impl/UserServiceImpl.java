package com.vmg.scrum.service.impl;


import com.vmg.scrum.exception.custom.LockAccountException;
import com.vmg.scrum.model.ERole;
import com.vmg.scrum.model.Role;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.option.Department;
import com.vmg.scrum.payload.request.*;
import com.vmg.scrum.payload.response.JwtResponse;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.DepartmentRepository;
import com.vmg.scrum.repository.RoleRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.security.UserDetailsImpl;
import com.vmg.scrum.security.jwt.HashOneWay;
import com.vmg.scrum.security.jwt.JwtUtils;
import com.vmg.scrum.service.MailService;
import com.vmg.scrum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    HashOneWay encoder;

    private final JwtUtils jwtUtils;

    @Autowired
    MailService mailService;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    FileManagerService fileManagerService;


    public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, HashOneWay encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    private static String alphaNumericString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        Boolean check = userRepository.getById(userDetails.getId()).getCheckRootDisable();
        Boolean avalible = userRepository.getById(userDetails.getId()).getAvalible();
        if (avalible == false) throw new LockAccountException("Account have been lock by admin");
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles, userRepository.getById(userDetails.getId()), check);
    }

    @Override
    public MessageResponse registerUser(SignupRequest signUpRequest) throws MessagingException, UnsupportedEncodingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Email đã tồn tại!");
        }
        if (userRepository.existsByCode(signUpRequest.getCode())) {
            throw new RuntimeException("Mã nhân viên đã tồn tại!");
        }
        String genarate = alphaNumericString(8);
        Department department = departmentRepository.findByName(signUpRequest.getDepartment());
        //file

        String filename = fileManagerService.save(signUpRequest.getCover());
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(genarate),
                signUpRequest.getFullName(),
                signUpRequest.getGender(),
                filename,
                "VMG_"+signUpRequest.getCode(),
                department,
                signUpRequest.getStartWork(),
                signUpRequest.getEndWork()
        );
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "manage" -> {
                        Role manageRole = roleRepository.findByName(ERole.ROLE_MANAGE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(manageRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        mailService.sendEmailAccountInfo(signUpRequest.getUsername(), genarate);
        return new MessageResponse("Tạo tài khoản thành công!");
    }

    @Override
    public MessageResponse registerUserPasswordDefault(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Email đã tồn tại!");
        }
        if (userRepository.existsByCode(signUpRequest.getCode())) {
            throw new RuntimeException("Mã nhân viên đã tồn tại!");
        }
        String genarate = alphaNumericString(8);
        Department department = departmentRepository.findByName(signUpRequest.getDepartment());
        //file

        String filename = fileManagerService.save(signUpRequest.getCover());
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(genarate),
                signUpRequest.getFullName(),
                signUpRequest.getGender(),
                filename,
                signUpRequest.getCode(),
                department,
                signUpRequest.getStartWork(),
                signUpRequest.getEndWork()
        );
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "manage" -> {
                        Role manageRole = roleRepository.findByName(ERole.ROLE_MANAGE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(manageRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        //vmg123456
        user.setRootPassword(encoder.encode("vmg123456"));
        userRepository.save(user);
        return new MessageResponse("Tạo tài khoản thành công!");
    }

    @Override
    public MessageResponse updatePassword(ChangePasswordRequest changePasswordRequest) {

        Long id = changePasswordRequest.getId();
        String newPassword = changePasswordRequest.getNewPassword();
        Optional<User> users = userRepository.findById(id);
        User user = users.get();
        boolean check = user.getCheckRootDisable();
        if (user.getPassword() == null || user.getPassword() == "") {

            if (encoder.matches(changePasswordRequest.getOldPassword(), user.getRootPassword())) {
                if (encoder.matches(newPassword, user.getRootPassword())) {
                    throw new RuntimeException("Mật khẩu mới phải khác mật khẩu hiện tại");
                }
                if (!check) {
                    String encodedPassword = encoder.encode(newPassword);
                    user.setPassword(encodedPassword);
                    user.setRootPassword(encoder.encode(""));
                    user.setCheckRootDisable(true);
                    userRepository.save(user);
                }
                if (check) {
                    String encodedPassword = encoder.encode(newPassword);
                    user.setPassword(encodedPassword);
                    userRepository.save(user);
                }
            } else throw new RuntimeException("Mật khẩu hiện tại không chính xác");
        } else {

            if (encoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
                if (encoder.matches(newPassword, user.getPassword())) {
                    throw new RuntimeException("Mật khẩu mới phải khác mật khẩu hiện tại");
                }
                if (!check) {
                    String encodedPassword = encoder.encode(newPassword);
                    user.setPassword(encodedPassword);
                    user.setRootPassword(encoder.encode(""));
                    user.setCheckRootDisable(true);
                    userRepository.save(user);
                }
                if (check) {
                    String encodedPassword = encoder.encode(newPassword);
                    user.setPassword(encodedPassword);
                    userRepository.save(user);
                }
            } else throw new RuntimeException("Mật khẩu hiện tại không chính xác");
        }
        return new MessageResponse("Thay đổi mật khẩu thành công!");
    }

    @Override
    public void updateUser(long id, UpdateUserRequest updateRequest) {
        User user = userRepository.findById(id).get();

        if (!user.getUsername().equals(updateRequest.getUsername())) {
            if (userRepository.findByUsername(updateRequest.getUsername()).isPresent())
                throw new RuntimeException("Email đã tồn tại");
        }
        if (!user.getCode().equals(updateRequest.getCode())) {
            if (userRepository.findByCode(updateRequest.getCode()) != null)
                throw new RuntimeException("Mã nhân viên đã tồn tại");
        }

        user.setUsername(updateRequest.getUsername());
        user.setCode("VMG_"+updateRequest.getCode());
        user.setFullName(updateRequest.getFullName());
        user.setGender(updateRequest.getGender());
        Department department = departmentRepository.findByName(updateRequest.getDepartment());
        user.setDepartments(department);
        user.setStartWork(updateRequest.getStartWork());
        user.setEndWork(updateRequest.getEndWork());
        System.out.println(updateRequest.getCover().getSize());
        if (updateRequest.getCover() != null && updateRequest.getCover().getSize() > 0) {
            if(!user.getCover().equals("default.png"))
            fileManagerService.delete(user.getCover());
            String filename = fileManagerService.save(updateRequest.getCover());
            user.setCover(filename);
        }
        Set<String> strRoles = updateRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "manage" -> {
                        Role manageRole = roleRepository.findByName(ERole.ROLE_MANAGE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(manageRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public boolean checkValidateJWTEmail(String token) {
      return jwtUtils.validateJwtTokenEmail(token);
    }

    @Override
    public Page<User> manageUsers(ManageUser_Request manageUser_request, Pageable pageable) {
        Long departid = manageUser_request.getDepartid();
        Boolean status = manageUser_request.getStatus();
        String search = manageUser_request.getSearch();
        Page<User> pageUsers = null;

        if(departid!=null && departid!=0){
            if(status!=null ){
//                Boolean available = Boolean.parseBoolean(status);
                if(search!=null && search!=""){
                    pageUsers = userRepository.getUsersByDepartments_Id_Search_Status(departid, search, status,pageable);
                }
                else{
                    pageUsers = userRepository.getUsersByDepartments_Id_Status(departid, status, pageable);
                }
            }
            else{
                if(search!=null && search!=""){
                    pageUsers = userRepository.getUsersByDepartments_Id_Search(departid, search, pageable);
                }
                else{
                    pageUsers = userRepository.getUsersByDepartments_Id(departid, pageable);
                }
            }
        }
        else{
            if(status!=null ){
//                Boolean available = Boolean.parseBoolean(status);
                if(search!=null && search!=""){
                    System.out.println("aaaa");
                    pageUsers = userRepository.findAll_Search_Status(search,status, pageable);
                }
                else{
                    pageUsers = userRepository.findAll_Status(status, pageable);
                }
            }
            else{
                if(search!=null && search!=""){
                    pageUsers = userRepository.findAll_Search(search, pageable);
                }
                else{
                    pageUsers = userRepository.findAll(pageable);
                }
            }

        }
        return pageUsers;
    }

    @Override
    public MessageResponse lockAccount(Long id) {
        User user = userRepository.getById(id);
        user.setAvalible(!user.getAvalible());
        userRepository.save(user);
        if (user.getAvalible()) {
            user.setEndWork(null);
            userRepository.save(user);
            return new MessageResponse("Mở khóa tài khoản thành công!");
        } else {
            user.setEndWork(LocalDate.now());
            userRepository.save(user);
            return new MessageResponse("Khóa tài khoản thành công!");
        }
    }


    @Override
    public MessageResponse forgotPasswordChangeRequest(ForgotPasswordChangeRequest forgotPasswordChangeRequest) {
            String token = forgotPasswordChangeRequest.getToken();
            if (token != null) {
                User user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtTokenEmail(token)).get();
                if(!user.getCheckRootDisable()){
                    user.setRootPassword(encoder.encode(forgotPasswordChangeRequest.getNewPassword()));
                }
                if(!user.getAvalible())
                    throw  new RuntimeException("Tài khoản người dùng này đang bị khóa");
                user.setPassword(encoder.encode(forgotPasswordChangeRequest.getNewPassword()));
                userRepository.save(user);
            }

        return new MessageResponse("Đổi mật khẩu thành công");
    }

}