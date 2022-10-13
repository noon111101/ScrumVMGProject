package com.vmg.scrum.service.impl;


import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.security.UserDetailsServiceImpl;
import com.vmg.scrum.security.jwt.JwtUtils;
import com.vmg.scrum.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public MailServiceImpl(JavaMailSender mailSender, UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }


    public void sendEmail(String recipientEmail)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@shopme.com", "Shopme Support");
        helper.setTo(recipientEmail);
        String subject = "Here's the link to reset your password";
        String token = jwtUtils.generateJwtToken(SecurityContextHolder.getContext().getAuthentication());
        String resetPasswordLink = "http://localhost:3000" + "/reset_password?token=" + token;
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + resetPasswordLink + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public UserDetails resetPassword(String token) {
         return  userDetailsService.loadUserByUsername(jwtUtils.getUserNameFromJwtToken(token));
    }
}
