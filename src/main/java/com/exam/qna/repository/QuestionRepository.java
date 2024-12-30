package com.exam.qna.repository;

import com.exam.qna.entity.Question;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    Page<Question> findBySubjectContains(String kw, Pageable pageable);

    Page<Question> findByAuthorUsernameContaining(String kw, Pageable pageable);

    Page<Question> findByContentContaining(String kw, Pageable pageable);

}
