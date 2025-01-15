package com.exam.qna.service;

import com.exam.qna.entity.SiteUser;
import com.exam.qna.error.DataNotFoundException;
import com.exam.qna.error.SignupEmailDuplicatedException;
import com.exam.qna.error.SignupUsernameDuplicatedException;
import com.exam.qna.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteUserService {

    private final SiteUserRepository siteUserRepository;

    // 스프링이 PasswordEncoder 타입의 객체를 만들어야 하는 상황 죽, 객체의 생성 주기를 스프링에게 위임하는 방식으로,
    // 의존성 주입을 통해 PasswordEncoder 객체를 스프링이 관리
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password)
            throws SignupUsernameDuplicatedException, SignupEmailDuplicatedException {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);


        // 암호화
        // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));

        try {
            siteUserRepository.save(user);
        } catch (DataIntegrityViolationException e) {

            if (siteUserRepository.existsByUsername(username)) {
                throw new SignupUsernameDuplicatedException("이미 사용중인 이름입니다.");
            } else {
                throw new SignupEmailDuplicatedException("이미 사용중인 Email 입니다.");
            }
        }

        return user;
    }

    public SiteUser getUser(String name) {
        return siteUserRepository.findByUsername(name).orElseThrow(() ->
                new DataNotFoundException("user not found")
        );
    }
}
