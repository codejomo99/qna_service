package com.exam.qna.repository;

import com.exam.qna.entity.Question;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question,  Long>, RepositoryUtil {



    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE question AUTO_INCREMENT = 1;", nativeQuery = true)
    void truncate();

    Question findBySubject(String s);

    List<Question> findBySubjectLike(String s);
}
