package com.vmg.scrum.service;


import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.security.UserDetailsServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

public interface MailService {
    Boolean sendEmail(String recipientEmail) throws MessagingException, UnsupportedEncodingException;

    UserDetails resetPasswordToken(String token);

    Boolean resetPassword(String email) throws MessagingException, UnsupportedEncodingException;
    public void sendEmailAccountInfo(String recipientEmail,String rootPassword) throws MessagingException, UnsupportedEncodingException;

}
