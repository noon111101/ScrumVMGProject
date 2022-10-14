package com.vmg.scrum.service;


import com.vmg.scrum.entity.User;

import org.springframework.security.core.userdetails.UserDetails;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

public interface MailService {
    void sendEmail(String recipientEmail) throws MessagingException, UnsupportedEncodingException;

    UserDetails resetPassword(String token);
}
