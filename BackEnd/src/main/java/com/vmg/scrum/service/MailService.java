package com.vmg.scrum.service;


import com.vmg.scrum.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

public interface MailService {
    Boolean sendEmail(String recipientEmail) throws MessagingException, UnsupportedEncodingException;

  void sendEmailFollowers(Set<String> recipientEmail, String title, User fullName) throws MessagingException, UnsupportedEncodingException;

  void sendEmailApprovers(Set<String> recipientEmail, String title, User fullName) throws MessagingException, UnsupportedEncodingException;

  UserDetails resetPasswordToken(String token);

    Boolean resetPassword(String email) throws MessagingException, UnsupportedEncodingException;
    public void sendEmailAccountInfo(String recipientEmail,String rootPassword) throws MessagingException, UnsupportedEncodingException;



}
