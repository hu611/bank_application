package com.credit;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Bcryptencode {
    public static void main(String[] args) {
        String salt = "$2a$10$j.f6OtQKj/KNytqwZrE6vO";
        String a = BCrypt.hashpw("123",salt);
        String b = BCrypt.hashpw("123",salt);
        System.out.println(a + "\n" + b);
    }
}
