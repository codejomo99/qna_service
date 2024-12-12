package com.exam.qna;

import static org.assertj.core.api.Assertions.assertThat;

import com.exam.qna.repository.AnswerRepository;
import com.exam.qna.repository.QuestionRepository;
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
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;



    @BeforeEach
    void beforeEach() {
        clearData();
        createSampleData();
    }

    private void createSampleData(){
        createSampleData(userService);
    }
    private void createSampleData(SiteUserService userService){
        userService.create("admin","admin@test.com", "1234");
        userService.create("user1","user@test.com","1234");
    }

    private void clearData(){
        clearData(userRepository, answerRepository,questionRepository);
    }

    private void clearData(SiteUserRepository userRepository, AnswerRepository answerRepository, QuestionRepository questionRepository) {
        AnswerRepositoryTests.clearData(answerRepository,questionRepository);
        userRepository.deleteAll();
        userRepository.truncate();
    }

    @Test
    @DisplayName("회원가입 성공")
    public void sign(){
        userService.create("user2","user2@email.com","1234");
        userService.create("user3","user3@email.cin","1234");

        assertThat( userRepository.count()).isEqualTo(4);
    }

}
