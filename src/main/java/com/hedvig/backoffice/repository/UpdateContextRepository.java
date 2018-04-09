package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.UpdateContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UpdateContextRepository extends JpaRepository<UpdateContext, Long> {

    @Query("select uc from UpdateContext uc where uc.personnel = :personnel")
    Optional<UpdateContext> findByPersonnel(@Param("personnel") Personnel personnel);

    @Query("select uc from UpdateContext uc where uc.personnel.id = :id")
    Optional<UpdateContext> findByPersonnelId(@Param("id") String personnelId);

    @Query("select uc from UpdateContext uc where uc.sessionId = :id")
    Optional<UpdateContext> findBySessionId(@Param("id") String id);

    @Query("select uc from UpdateContext uc where uc.personnel.id = :personnelId and uc.sessionId = :sessionId and uc.subId = :subId")
    Optional<UpdateContext> findByPersonnelIdAndSessionIdAndSubId(@Param("personnelId") String personnelId,
                                                                  @Param("sessionId") String sessionId,
                                                                  @Param("subId") String subId);

}
