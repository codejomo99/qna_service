package com.exam.qna;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.exam.qna.entity.Answer;
import com.exam.qna.entity.Question;
import com.exam.qna.repository.AnswerRepository;
import com.exam.qna.repository.QuestionRepository;
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


    @BeforeEach
    void beforeEach() {
        clearData();
        createSampleData();
    }

    void createSampleData() {
        int questId = QuestionRepositoryTests.createSampleData(questionRepository);
        Question q = questionRepository.findById(questId).get();

        Answer a = new Answer();
        a.setContent("저도 잘 몰라요");
        a.setCreateDate(LocalDateTime.now());
        a.setQuestion(q);
        answerRepository.save(a);

        // 답변객체를 질문에 담아준다. (양방향 관계)
        q.getAnswerList().add(a);


        Answer a1 = new Answer();
        a1.setContent("아 저는 알아요");
        a1.setCreateDate(LocalDateTime.now());
        a1.setQuestion(q);
        answerRepository.save(a1);

        // 답변객체를 질문에 담아준다. (양방향 관계)
        q.getAnswerList().add(a1);
    }

    void clearData() {
        QuestionRepositoryTests.clearData(questionRepository);
        // 질문 외래키 삭제
        //answerRepository.disableForeignKeyChecks();
        // Delete -> ALTER TABLE answer AUTO_INCREMENT = 1; 바꾼다.
        answerRepository.deleteAll();
        // 답변 초기화
        answerRepository.truncate();
        // 질문 외래키 다시 만든다
        //answerRepository.enableForeignKeyChecks();
    }

    // save
    @Test
    void save() {

        Question q = questionRepository.findById(2).get();

        Answer a = new Answer();
        a.setContent("음..그러게요");
        a.setCreateDate(LocalDateTime.now());
        a.setQuestion(q);
        answerRepository.save(a);
        assertThat(a.getId()).isEqualTo( 2);
        assertThat(a.getQuestion().getId()).isEqualTo(2);

    }

    // update
    @Test
    void update() {

        Answer a = answerRepository.findById(1).get();
        a.setContent("답변이 수정되었습니다.");
        answerRepository.save(a);

        assertThat(a.getContent()).isEqualTo("답변이 수정되었습니다.");
    }

    // delete
    @Test
    void delete() {
        assertThat(answerRepository.count()).isEqualTo(1);

        Answer q = answerRepository.findById(1).get();

        answerRepository.delete(q);
        assertEquals(0, answerRepository.count());

    }

    @Test
    void find(){
        Answer a  = answerRepository.findById(1).get();

        assertThat(a.getContent()).isEqualTo("저도 잘 몰라요");
    }


    // 답변을 가져올 때는 관련된 질문도 같이 가져와진다.
    @Test
    void answer으로부터_관련된_질문_조회(){

        // 답변으로 부터 질문을 찾는다.
        Answer a  = answerRepository.findById(1).get();
        Question q = a.getQuestion();

        assertThat(q.getId()).isEqualTo(2);
    }

    /**
     동일 트렌잭션 안에서는 내에서는 동일 객체가 반환된다.
     **/

    @Test
    @Transactional // beforeEach 까지 실행
    @Rollback(value = false)
    void question으로부터_관련된_답변들_조회(){

        // Error : failed to lazily initialize a collection of role
        Question q = questionRepository.findById(2).get();
        // 중간에 Question DB 연결이 끊킨다.


        List<Answer> answerList = q.getAnswerList();

        assertThat(answerList.size()).isEqualTo(2);
        assertThat(answerList.get(0).getContent()).isEqualTo("저도 잘 몰라요");
    }

}
