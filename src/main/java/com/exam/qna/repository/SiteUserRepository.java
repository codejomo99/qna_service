package com.exam.qna.repository;

import com.exam.qna.entity.SiteUser;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteUserRepository extends JpaRepository<SiteUser, Long> ,RepositoryUtil {

    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE site_user AUTO_INCREMENT = 1;", nativeQuery = true)
    void truncate();

    boolean existsByUsername(String username);

    Optional<SiteUser> findByUsername(String username);
}
