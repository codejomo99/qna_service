package com.exam.qna;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.exam.qna.entity.Answer;
import com.exam.qna.entity.Question;
import com.exam.qna.entity.User;
import com.exam.qna.repository.AnswerRepository;
import com.exam.qna.repository.QuestionRepository;
import com.exam.qna.repository.SiteUserRepository;
import com.exam.qna.service.SiteUserService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class AnswerRepositoryTests {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SiteUserRepository userRepository;

    @Autowired
    private SiteUserService userService;


    @BeforeEach
    void beforeEach() {
        clearData();
        createSampleData();
    }

    void createSampleData() {
        Long questId = QuestionRepositoryTests.createSampleData(userService,questionRepository);
        Question q = questionRepository.findById(questId).get();

        Answer a = new Answer();
        a.setContent("저도 잘 몰라요");
        a.setCreateDate(LocalDateTime.now());
        a.setAuthor(new User(1L));
        // 답변객체를 질문에 담아준다. (양방향 관계)
        q.addAnswer(a);
        answerRepository.save(a);


        Answer a1 = new Answer();
        a1.setContent("아 저는 알아요");
        a1.setCreateDate(LocalDateTime.now());
        // 답변객체를 질문에 담아준다. (양방향 관계)
        a1.setAuthor(new User(2L));
        q.addAnswer(a1);
        answerRepository.save(a1);


        questionRepository.save(q);
    }

    private void clearData(){
        clearData(userRepository, answerRepository,questionRepository);
    }

    public static void clearData(SiteUserRepository userRepository, AnswerRepository answerRepository, QuestionRepository questionRepository) {
        UserServiceTests.clearData(userRepository, answerRepository, questionRepository);

    }

    // save
    @Test
    @Transactional
    @Rollback(value = false)
    void save() {

        Question q = questionRepository.findById(2L).get();

        Answer a = new Answer();
        a.setContent("음..그러게요");
        a.setCreateDate(LocalDateTime.now());
        a.setAuthor(new User(2L));
        q.addAnswer(a);

        // CascadeType.ALL 을 적용해서 조금 더 객체지향적으로
        questionRepository.save(q);
    }

    // update
    @Test
    @Transactional // beforeEach 포함해서 실행
    @Rollback(value = false)
    void update() {

        Answer a = answerRepository.findById(1L).get();
        a.setContent("답변이 수정되었습니다.");
        answerRepository.save(a);

        assertThat(a.getContent()).isEqualTo("답변이 수정되었습니다.");
    }

    // delete
    @Test
    @Transactional
    @Rollback(value = false)
    void delete() {
        assertThat(answerRepository.count()).isEqualTo(2);

        Answer q = answerRepository.findById(1L).get();

        answerRepository.delete(q);
        assertEquals(2, answerRepository.count());

    }

    @Test
    @Transactional
    @Rollback(value = false)
    void find(){
        Answer a  = answerRepository.findById(1L).get();

        assertThat(a.getContent()).isEqualTo("저도 잘 몰라요");
    }


    // 답변을 가져올 때는 관련된 질문도 같이 가져와진다.
    @Test
    @Transactional
    @Rollback(value = false)
    void answer으로부터_관련된_질문_조회(){

        // 답변으로 부터 질문을 찾는다.
        Answer a  = answerRepository.findById(1L).get();
        Question q = a.getQuestion();

        assertThat(q.getId()).isEqualTo(2);
    }

    /**
     동일 트렌잭션 안에서는 내에서는 동일 객체가 반환된다.
     **/

    @Test
    @Transactional
    @Rollback(value = false)
    void question으로부터_관련된_답변들_조회(){

        // Error : failed to lazily initialize a collection of role
        Question q = questionRepository.findById(2L).get();
        // 중간에 Question DB 연결이 끊킨다.


        List<Answer> answerList = q.getAnswerList();

        assertThat(answerList.size()).isEqualTo(2);
        assertThat(answerList.get(0).getContent()).isEqualTo("저도 잘 몰라요");
    }


}
