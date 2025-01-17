package com.exam.qna.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig { // passwordConfig

    @Bean
    public PasswordEncoder passwordEncoder() { // DI 주입 받기위한 !

        // 비밀번호를 암호화하는 해쉬함수 BCryptPasswordEncoder()
        return new BCryptPasswordEncoder();
    }
}
