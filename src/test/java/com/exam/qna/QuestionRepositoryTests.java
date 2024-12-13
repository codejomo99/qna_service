package com.exam.qna;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.exam.qna.entity.Question;
import com.exam.qna.entity.SiteUser;
import com.exam.qna.repository.AnswerRepository;
import com.exam.qna.repository.QuestionRepository;
import com.exam.qna.repository.SiteUserRepository;
import com.exam.qna.service.SiteUserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class QuestionRepositoryTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private SiteUserRepository userRepository;

    @Autowired
    private SiteUserService userService;


    private static Long lastSampleDataId;

    @BeforeEach
    void beforeEach(){
        clearData();
        createSampleData();
    }

    public static long createSampleData(SiteUserService userService, QuestionRepository questionRepository) {
        UserServiceTests.createSampleData(userService);

        Question q1 = new Question();
        q1.setSubject("이게 무엇인가요?");
        q1.setContent("이것에 대해서 알고 싶습니다.");
        q1.setAuthor(new SiteUser(2L));
        q1.setCreateDate(LocalDateTime.now());
        questionRepository.save(q1);  // 첫번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setAuthor(new SiteUser(2L));
        q2.setCreateDate(LocalDateTime.now());
        questionRepository.save(q2);  // 두번째 질문 저장

        return lastSampleDataId = q2.getId();
    }

    public void createSampleData(){
        lastSampleDataId = createSampleData(userService,questionRepository);
    }

    private void clearData(){
        clearData(userRepository, answerRepository,questionRepository);
    }
    public static void clearData(SiteUserRepository userRepository, AnswerRepository answerRepository, QuestionRepository questionRepository) {
        UserServiceTests.clearData(userRepository,answerRepository,questionRepository);
    }

    // save
    @Test
    void save(){
        List<Question> list = questionRepository.findAll();
        assertEquals(2,list.size());

        Question q1 = new Question();
        q1.setSubject("이게 무엇인가요?");
        q1.setContent("이것에 대해서 알고 싶습니다.");
        q1.setAuthor(new SiteUser(2L));
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 세번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setAuthor(new SiteUser(2L));
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 네번째 질문 저장

        assertThat(q1.getId()).isEqualTo(lastSampleDataId+1);
        assertThat(q2.getId()).isEqualTo(lastSampleDataId+2);
    }

    // update
    @Test
    void update(){
        Question q = questionRepository.findById(1L).get();
        q.setSubject("수정되었습니다.");
        questionRepository.save(q);

        q = questionRepository.findById(1L).get();
        assertEquals("수정되었습니다.",q.getSubject());
    }

    // delete
    @Test
    void delete(){
        assertThat(questionRepository.count()).isEqualTo(lastSampleDataId);
        Question q = questionRepository.findById(1L).get();

        questionRepository.delete(q);
        assertEquals(1,questionRepository.count());

    }

    @Test
    void Pageable(){
        Pageable pageable = PageRequest.of(0,2);
        Page<Question> all = questionRepository.findAll(pageable);
        assertThat(all.getTotalPages()).isEqualTo(1);

    }

    @Test
    public void createManyData(){

        boolean run = true;
        if(run == false) return;

        IntStream.rangeClosed(3,300).forEach(id -> {
            Question q = new Question();
            q.setSubject("%d번 질문".formatted(id));
            q.setContent("%d번 질문의 내용".formatted(id));
            q.setCreateDate(LocalDateTime.now());
            q.setAuthor(new SiteUser(2L));
            questionRepository.save(q);
        });
    }

}
