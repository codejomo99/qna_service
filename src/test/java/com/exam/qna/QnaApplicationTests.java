package com.exam.qna;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.exam.qna.entity.Question;
import com.exam.qna.repository.QuestionRepository;
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
	void testJpa2(){
		List<Question> all = questionRepository.findAll();
		assertEquals(2,all.size());

		Question q = all.get(0);
		assertEquals("이게 무엇인가요?",q.getSubject());
	}

	// select * from question AS q where q.subject=?
	@Test
	void testJpa3(){
		Question q = this.questionRepository.findBySubject("이게 무엇인가요?");
		assertEquals(1,q.getId());
	}

}
