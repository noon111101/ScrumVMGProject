package com.vmg.scrum.service.impl;




import com.vmg.scrum.exception.custom.LockAccountException;
import com.vmg.scrum.model.ERole;
import com.vmg.scrum.model.Role;
import com.vmg.scrum.model.User;
import com.vmg.scrum.model.option.Department;
import com.vmg.scrum.payload.request.ChangePasswordRequest;
import com.vmg.scrum.payload.request.LoginRequest;
import com.vmg.scrum.payload.request.SignupRequest;
import com.vmg.scrum.payload.request.UpdateUserRequest;
import com.vmg.scrum.payload.response.JwtResponse;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.DepartmentRepository;
import com.vmg.scrum.repository.RoleRepository;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.security.UserDetailsImpl;
import com.vmg.scrum.security.jwt.JwtUtils;
import com.vmg.scrum.service.MailService;
import com.vmg.scrum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    @Autowired
    MailService mailService;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    FileManagerService fileManagerService;
    public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
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
        if (avalible==false) throw new LockAccountException("Account have been lock by admin");
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles,userRepository.getById(userDetails.getId()),check);
    }

    @Override
    public MessageResponse registerUser(SignupRequest signUpRequest) throws MessagingException, UnsupportedEncodingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {

             throw  new RuntimeException("Email is already taken!");
        }
        if (userRepository.existsByCode(signUpRequest.getCode())) {
            return new MessageResponse("Error: Code is already taken!");

        }
        String genarate =alphaNumericString(8);
        Department department = departmentRepository.findByName(signUpRequest.getDepartment());
        //file
        String filename = "default.png";
        if(signUpRequest.getCover()!=null)
         filename = fileManagerService.save("images",signUpRequest.getCover());
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(genarate),
                signUpRequest.getFullName(),
                signUpRequest.getGender(),
                filename,
                signUpRequest.getCode(),
                department
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
        mailService.sendEmailAccountInfo(signUpRequest.getUsername(),genarate);
        return new MessageResponse("User registered successfully!");
    }

    @Override
    public Boolean updatePassword(ChangePasswordRequest changePasswordRequest) {
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            Long id = changePasswordRequest.getId();
            String newPassword = changePasswordRequest.getNewPassword();
            Optional<User> users = userRepository.findById(id);
            User user = users.get();
            boolean check = user.getCheckRootDisable();
            if(user.getPassword()==null || user.getPassword()==""){
                if(passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getRootPassword() )){
                    if(!check){
                        String encodedPassword = passwordEncoder.encode(newPassword);
                        user.setPassword(encodedPassword);
                        user.setRootPassword(passwordEncoder.encode(""));
                        user.setCheckRootDisable(true);
                        userRepository.save(user);
                    }
                    if(check){
                        String encodedPassword = passwordEncoder.encode(newPassword);
                        user.setPassword(encodedPassword);
                        userRepository.save(user);
                    }}
                else return false;
            }
            else{
                if(passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword() )){
                    if(!check){
                        String encodedPassword = passwordEncoder.encode(newPassword);
                        user.setPassword(encodedPassword);
                        user.setRootPassword(passwordEncoder.encode(""));
                        user.setCheckRootDisable(true);
                        userRepository.save(user);
                    }
                    if(check){
                        String encodedPassword = passwordEncoder.encode(newPassword);
                        user.setPassword(encodedPassword);
                        userRepository.save(user);
                    }}
                else return false;
            }
            return true;
        }
        catch (Exception e){
            return false;
        }
}

    @Override
    public void updateUser(long id, UpdateUserRequest updateRequest) {
        User user = userRepository.findById(id).get();
        user.setCode(updateRequest.getCode());
        user.setFullName(updateRequest.getFullName());
        user.setUsername(updateRequest.getUsername());
        user.setGender(updateRequest.getGender());
        Department department = departmentRepository.findByName(updateRequest.getDepartment());
        user.setDepartments(department);
        String filename = fileManagerService.save("images",updateRequest.getCover());
        user.setCover(filename);
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
    public MessageResponse lockAccount(Long id) {
        User user = userRepository.getById(id);
        user.setAvalible(!user.getAvalible());
        userRepository.save(user);
        if(user.getAvalible()==true){
            return new MessageResponse("Mở khóa tài khoản thành công!");
        }
        else{
            return new MessageResponse("Khóa tài khoản thành công!");
        }
    }

}