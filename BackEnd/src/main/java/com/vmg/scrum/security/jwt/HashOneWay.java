package com.vmg.scrum.security.jwt;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class HashOneWay implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(
                rawPassword.toString().getBytes(StandardCharsets.UTF_8));
        String encodedPassword = new String(Hex.encode(hash));
        return encodedPassword;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
         HashOneWay hashOneWay =new HashOneWay();
         String password = hashOneWay.encode(rawPassword);
         if(password.equalsIgnoreCase(encodedPassword))
             return true;
         else return false;
    }


    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return PasswordEncoder.super.upgradeEncoding(encodedPassword);
    }
}
