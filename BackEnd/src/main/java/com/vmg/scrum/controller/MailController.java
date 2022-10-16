package com.vmg.scrum.controller;

import com.vmg.scrum.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@RestController
@RequestMapping("/api/mail")
public class MailController {
    @Autowired
    MailService mailService;
    @GetMapping("send")
    public boolean sendmail() throws MessagingException, UnsupportedEncodingException {

           mailService.sendEmail("trieu11112001@gmail.com");
           return true;

    }
    @PostMapping("/reset_password")
    public UserDetails resetpassword(@RequestParam(name ="token") String token){
        return mailService.resetPassword(token);
    }


}
