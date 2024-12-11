package com.exam.qna;

import static org.assertj.core.api.Assertions.assertThat;

import com.exam.qna.repository.SiteUserRepository;
import com.exam.qna.service.SiteUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private SiteUserService userService;
    @Autowired
    private SiteUserRepository userRepository;



    @BeforeEach
    void beforeEach(){
        UserServiceTests.clearData(userRepository);
    }

    public static void clearData(SiteUserRepository siteUserRepository){
        siteUserRepository.deleteAll();
        siteUserRepository.resetIncrement();
    }
    @Test
    @DisplayName("회원가입 성공")
    public void sign(){
        userService.create("user1","user@email.com","1234");
        userService.create("user2","user2@email.cin","1234");

        assertThat( userRepository.count()).isEqualTo(2);
    }

}
