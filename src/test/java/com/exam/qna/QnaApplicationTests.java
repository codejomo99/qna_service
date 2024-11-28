package com.exam.qna;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.exam.qna.entity.Question;
import com.exam.qna.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QnaApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void testJpa() {

		// 외래키를 없애고
        questionRepository.disableForeignKeyChecks();
		// 초기화
        questionRepository.truncate();
		// 외래키를 다시 만든다
        questionRepository.enableForeignKeyChecks();
    }

    @Test
    void testJpa1() {
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


    }

    // select * form question;
    @Test
    void testJpa2() {
        List<Question> all = questionRepository.findAll();
        assertEquals(2, all.size());

        Question q = all.get(0);
        assertEquals("이게 무엇인가요?", q.getSubject());
    }

    // select * from question AS q where q.subject=?
    @Test
    void testJpa3() {
        Question q = this.questionRepository.findBySubject("이게 무엇인가요?");
        assertEquals(1, q.getId());
    }

    @Test
    void testJpa4() {
        List<Question> list = questionRepository.findBySubjectLike("이게%");
        Question q = list.get(0);
        assertEquals("이게 무엇인가요?", q.getSubject());
    }

    @Test
    void testJpa5() {
        Question q = questionRepository.findBySubject("이게 무엇인가요?");
        questionRepository.delete(q);

        List<Question> list = questionRepository.findAll();
        assertEquals(1, list.size());
    }

    @Test
    @Transactional
    void testJpa6() {

        Question q = new Question();
        q.setContent("안녕하세요");
        q.setSubject("반갑습니다");

        questionRepository.save(q);

        Question find = questionRepository.findBySubject("반갑습니다");
        questionRepository.delete(find);

        List<Question> list = questionRepository.findAll();
        assertEquals(0, list.size());
    }

}
