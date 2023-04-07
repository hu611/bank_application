package com.example;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Bcryptencode {
    public static void main(String[] args) {
        String salt = "$2a$10$j.f6OtQKj/KNytqwZrE6vO";
        String a = BCrypt.hashpw("123",salt);
        String b = BCrypt.hashpw("123",salt);
        System.out.println(a + "\n" + b);
    }
}
