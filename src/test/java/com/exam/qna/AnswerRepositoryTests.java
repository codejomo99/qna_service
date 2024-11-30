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
    }

    void clearData() {
        QuestionRepositoryTests.clearData(questionRepository);
        // 질문 외래키 삭제
        answerRepository.disableForeignKeyChecks();
        // 답변 초기화
        answerRepository.truncate();
        // 질문 외래키 다시 만든다
        answerRepository.enableForeignKeyChecks();
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


}
