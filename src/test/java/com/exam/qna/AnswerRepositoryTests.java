package com.exam.qna;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.exam.qna.entity.Answer;
import com.exam.qna.entity.Question;
import com.exam.qna.repository.AnswerRepository;
import com.exam.qna.repository.QuestionRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AnswerRepositoryTests {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private static int lastSampleDataId;

    @BeforeEach
    void beforeEach() {
        clearData();
        createSampleData();
    }

    void createSampleData() {
        Question q1 = new Question();
        q1.setSubject("이게 무엇인가요?");
        q1.setContent("이것에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 첫번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 두번째 질문 저장


        Answer a = new Answer();
        a.setContent("저도 잘 몰라요");
        a.setCreateDate(LocalDateTime.now());
        a.setQuestion(q2);
        answerRepository.save(a); // 첫번째 답변 저장

        lastSampleDataId = a.getId();


    }

    void clearData() {
        answerRepository.disableForeignKeyChecks();
        // 초기화
        answerRepository.truncate();
        // 외래키를 다시 만든다
        answerRepository.enableForeignKeyChecks();
    }

    // save
    @Test
    void save() {

        Question q = questionRepository.findById(2).get();

        Answer a = new Answer();
        a.setContent("저도 잘 몰라요");
        a.setCreateDate(LocalDateTime.now());
        a.setQuestion(q);
        answerRepository.save(a);

        assertThat(a.getId()).isEqualTo(lastSampleDataId+1);
        assertThat(a.getQuestion().getId()).isEqualTo(2);

    }

    // update
    @Test
    void update() {

        Answer a = answerRepository.findById(lastSampleDataId).get();
        a.setContent("답변이 수정되었습니다.");
        answerRepository.save(a);

        assertThat(a.getContent()).isEqualTo("답변이 수정되었습니다.");
    }

    // delete
    @Test
    void delete() {
        assertThat(answerRepository.count()).isEqualTo(lastSampleDataId);

        Answer q = answerRepository.findById(lastSampleDataId).get();

        answerRepository.delete(q);
        assertEquals(lastSampleDataId-1, answerRepository.count());

    }


}
