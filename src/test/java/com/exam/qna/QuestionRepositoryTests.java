package com.exam.qna;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.exam.qna.entity.Question;
import com.exam.qna.repository.QuestionRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QuestionRepositoryTests {

    @Autowired
    private QuestionRepository questionRepository;

    private static int lastSampleDataId;

    @BeforeEach
    void beforeEach(){
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

        lastSampleDataId = q2.getId();


    }

    void clearData() {
        questionRepository.disableForeignKeyChecks();
        // 초기화
        questionRepository.truncate();
        // 외래키를 다시 만든다
        questionRepository.enableForeignKeyChecks();
    }

    // save
    @Test
    void save(){
        List<Question> list = questionRepository.findAll();
        assertEquals(2,list.size());

        Question q1 = new Question();
        q1.setSubject("이게 무엇인가요?");
        q1.setContent("이것에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 세번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 네번째 질문 저장

        assertThat(q1.getId()).isEqualTo(lastSampleDataId+1);
        assertThat(q2.getId()).isEqualTo(lastSampleDataId+2);
    }

    // update
    @Test
    void update(){
        Question q = questionRepository.findById(1).get();
        q.setSubject("수정되었습니다.");
        questionRepository.save(q);

        q = questionRepository.findById(1).get();
        assertEquals("수정되었습니다.",q.getSubject());
    }

    // delete
    @Test
    void delete(){
        assertThat(questionRepository.count()).isEqualTo(lastSampleDataId);
        Question q = questionRepository.findById(1).get();

        questionRepository.delete(q);
        assertEquals(1,questionRepository.count());

    }

}
