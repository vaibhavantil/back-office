package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.ChatContext;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatContextRepository extends CrudRepository<ChatContext, String> {

    @Query("select c from ChatContext c where c.subId = :subId and c.sessionId = :sessionId")
    Optional<ChatContext> findBySubIdAndSessionId(@Param("subId") String subId, @Param("sessionId") String sessionId);

    @Query("select c from ChatContext c where c.sessionId = :sessionId")
    List<ChatContext> findBySessionId(@Param("sessionId") String sessionId);

    @Query("select c from ChatContext c where c.hid = :hid")
    Optional<ChatContext> finByHid(@Param("hid") String hid);

}
