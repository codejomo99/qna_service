package com.exam.qna.repository;

import com.exam.qna.entity.Answer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long>, RepositoryUtil {
    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE answer AUTO_INCREMENT = 1;", nativeQuery = true)
    void truncate();
}
