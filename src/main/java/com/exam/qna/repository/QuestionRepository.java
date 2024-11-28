package com.exam.qna.repository;

import com.exam.qna.entity.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question,  Integer> {
    Question findBySubject(String s);

    List<Question> findBySubjectLike(String s);
}
