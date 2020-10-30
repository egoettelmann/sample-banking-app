package com.github.egoettelmann.sample.auth.api;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

    @Test
    public void test() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password1 = passwordEncoder.encode("password1");
        String password2 = passwordEncoder.encode("password2");
        System.out.println("Password for user1: " + password1);
        System.out.println("Password for user2: " + password2);
    }

}
