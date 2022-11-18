package com.vmg.scrum.service.impl;


import com.vmg.scrum.model.User;
import com.vmg.scrum.payload.response.MessageResponse;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.security.UserDetailsServiceImpl;
import com.vmg.scrum.security.jwt.HashOneWay;
import com.vmg.scrum.security.jwt.JwtUtils;
import com.vmg.scrum.service.MailService;
import com.vmg.scrum.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Value("${application.domain.cors}")
    private String domain ;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    HashOneWay passwordEncoder;

    public MailServiceImpl(JavaMailSender mailSender, UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
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
    public Boolean sendEmail(String recipientEmail) {
    try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("VMG@contact.com", "VMG");
        helper.setTo(recipientEmail);
        String subject = "Here's the link to reset your password";
        String emailToken = jwtUtils.generateJwtTokenEmail(recipientEmail);
        String resetPasswordLink = domain + "/reset_password-tokenLink?token=" +emailToken;
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + resetPasswordLink + "\">Click here to direct change your password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    } catch (Exception e){
        return false;
    }
    }
    @Override
    public void sendEmailAccountInfo(String recipientEmail, String rootPassword)
            throws MessagingException, UnsupportedEncodingException {
         MimeMessage message = mailSender.createMimeMessage();
         MimeMessageHelper helper = new MimeMessageHelper(message);

         helper.setFrom("VMG@mailnotifi", "VMG");
         helper.setTo(recipientEmail);
         String subject = "Here's the info account";
         String content = "<p>Hello,</p>"
                 + "<p>You have to logIn and change your password.</p>"
                 + "Here is account infomation :"
                 + "<p> Email:yourEmail </p>"
                 + "<br>"
                 + "<p> Password:" + rootPassword + "</p>";

         helper.setSubject(subject);

         helper.setText(content, true);

         mailSender.send(message);

    }

    @Override
    public UserDetails resetPasswordToken(String token) {
         return  userDetailsService.loadUserByUsername(jwtUtils.getUserNameFromJwtToken(token));
    }

    @Override
    public Boolean resetPassword(String email) throws MessagingException, UnsupportedEncodingException {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            User user = userRepository.findByUsername(email).get();
            String rootPassword = alphaNumericString(8);
            user.setRootPassword(passwordEncoder.encode(rootPassword));
            user.setCheckRootDisable(false);
            userRepository.save(user);
            helper.setFrom("VMG@mailnotifi", "VMG");
            helper.setTo(email);
            String subject = "Here's the info account";
            String content = "<p>Hello,</p>"
                    + "<p>You password have been reset.</p>"
                    + "Here is account infomation :"
                    + "<p> Email:yourEmail </p>"
                    + "<br>"
                    + "<p> Password:" + rootPassword +"</p>";

            helper.setSubject(subject);

            helper.setText(content, true);

            mailSender.send(message);
            return true;
        }
        catch (Exception e){
            return false;
        }

    }
}
