package com.vmg.scrum.controller;

import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/mail")
public class MailController {
    @Autowired
    MailService mailService;
    @GetMapping("send")
    public MessageResponse sendmail() throws MessagingException, UnsupportedEncodingException {

         return  mailService.sendEmail("trieu11112001@gmail.com");
    }
    @PostMapping("reset_password-tokenLink")
    public UserDetails resetpasswordtoken(@RequestParam(name ="token") String token){
        return mailService.resetPasswordToken(token);
    }
    @PostMapping("reset_password")
    public Boolean resetpassword(@RequestParam(name ="email") String email) throws MessagingException, UnsupportedEncodingException {
        return mailService.resetPassword(email);
    }

}
