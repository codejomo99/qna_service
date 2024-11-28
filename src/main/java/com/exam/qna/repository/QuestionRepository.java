package com.exam.qna.repository;

import com.exam.qna.entity.Question;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question,  Integer> {



    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE question", nativeQuery = true)
    void truncate();

    Question findBySubject(String s);

    List<Question> findBySubjectLike(String s);


    @Transactional
    @Modifying
    @Query(value = "SET FOREIGN_KEY_CHECKS = 0", nativeQuery = true)
    void disableForeignKeyChecks();

    @Transactional
    @Modifying
    @Query(value = "SET FOREIGN_KEY_CHECKS = 1", nativeQuery = true)
    void enableForeignKeyChecks();
}
