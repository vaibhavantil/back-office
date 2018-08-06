package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.domain.Personnel;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatContextRepository extends CrudRepository<ChatContext, String> {

  @Modifying
  @Query("delete from ChatContext c where c.subId = :subId and c.sessionId = :sessionId")
  void deleteBySubIdAndSessionId(
      @Param("subId") String subId, @Param("sessionId") String sessionId);

  @Modifying
  @Query("delete from ChatContext c where c.sessionId = :sessionId")
  void deleteBySessionId(@Param("sessionId") String sessionId);

  @Query("select c from ChatContext c where c.active = true")
  List<ChatContext> findActiveChats();

  @Query(
      "select distinct c.personnel from ChatContext c where c.memberId = :memberId and c.active = true")
  List<Personnel> findPersonnelsWithActiveChatsByMemberId(@Param("memberId") String memberId);
}
