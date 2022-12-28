package com.vmg.scrum.service;


import com.vmg.scrum.model.User;
import com.vmg.scrum.model.request.Request;
import org.springframework.security.core.userdetails.UserDetails;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public interface MailService {
    Boolean sendEmail(String recipientEmail) throws MessagingException, UnsupportedEncodingException;

  void sendEmailFollowers(Set<String> recipientEmail, String title, String department, User fullName, Request totalsDay, LocalTime timeStart, LocalDate dateFrom, LocalTime timeEnd, LocalDate dateTo) throws MessagingException, UnsupportedEncodingException;


  void sendEmailFollowersForget(Set<String> recipientEmail, String title, String department, Request totalsDay, User fullName, LocalDate dateForget) throws MessagingException, UnsupportedEncodingException;

  void sendEmailFollowersTCS(Set<String> recipientEmail, String title, String department, Request totalsDay, User fullName, LocalDate dateFrom, LocalDate dateTo) throws MessagingException, UnsupportedEncodingException;

  void sendEmailApprovers(Set<String> recipientEmail, String title, String department, User fullName, Request totalsDay, LocalTime timeStart, LocalDate dateFrom, LocalTime timeEnd, LocalDate dateTo) throws MessagingException, UnsupportedEncodingException;

  void sendEmailApproversForget(Set<String> recipientEmail, String title, String department, Request totalsDay, User fullName, LocalDate dateForget) throws MessagingException, UnsupportedEncodingException;

  void sendEmailApproversTCS(Set<String> recipientEmail, String title, String department, Request totalsDay, User fullName, LocalDate dateFrom, LocalDate dateTo) throws MessagingException, UnsupportedEncodingException;

  UserDetails resetPasswordToken(String token);

    Boolean resetPassword(String email) throws MessagingException, UnsupportedEncodingException;
    public void sendEmailAccountInfo(String recipientEmail,String rootPassword) throws MessagingException, UnsupportedEncodingException;



}
