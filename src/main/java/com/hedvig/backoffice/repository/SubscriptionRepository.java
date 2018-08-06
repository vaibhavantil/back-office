package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.Subscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  @Query("select s from Subscription s where s.memberId = :memberId")
  Optional<Subscription> findByMemberId(@Param("memberId") String memberId);

  @Query("select s from Subscription s where s.chats.size > 0")
  List<Subscription> findActiveSubscriptions();
}
