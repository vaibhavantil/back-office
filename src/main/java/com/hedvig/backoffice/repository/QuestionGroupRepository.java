package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.QuestionGroup;
import com.hedvig.backoffice.domain.Subscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionGroupRepository extends JpaRepository<QuestionGroup, Long> {

  @Query("select g from QuestionGroup g where g.answer is null and g.subscription = :subscription")
  Optional<QuestionGroup> findUnasweredBySub(@Param("subscription") Subscription subscription);

  @Query(
      "select g from QuestionGroup g where g.answer is null and g.subscription.memberId = :memberId")
  Optional<QuestionGroup> findUnasweredByMemberId(@Param("memberId") String memberId);

  @Query("select g from QuestionGroup g where g.answer is not null order by g.date")
  List<QuestionGroup> answered();

  @Query("select g from QuestionGroup g where g.answer is null order by g.date")
  List<QuestionGroup> notAnswered();

  @Query("select count(g) from QuestionGroup g where g.answer is null")
  Long notAnsweredCount();
}
