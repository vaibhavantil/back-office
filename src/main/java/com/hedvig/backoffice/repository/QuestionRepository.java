package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select q from Question q where q.id = :id")
    Optional<Question> findById(@Param("id") Long id);

}
