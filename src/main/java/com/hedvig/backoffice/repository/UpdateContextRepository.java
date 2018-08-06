package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.UpdateContext;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateContextRepository extends JpaRepository<UpdateContext, Long> {

  @Query("select uc from UpdateContext uc where uc.personnel.id = :id")
  List<UpdateContext> findByPersonnelId(@Param("id") String personnelId);

  @Query("select uc from UpdateContext uc where uc.sessionId = :id")
  Optional<UpdateContext> findBySessionId(@Param("id") String id);

  @Query(
      "select uc from UpdateContext uc where uc.personnel.id = :personnelId and uc.sessionId = :sessionId")
  Optional<UpdateContext> findByPersonnelIdAndSessionId(
      @Param("personnelId") String personnelId, @Param("sessionId") String sessionId);
}
