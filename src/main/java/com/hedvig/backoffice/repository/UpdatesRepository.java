package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.UpdateContext;
import com.hedvig.backoffice.domain.Updates;
import com.hedvig.backoffice.services.updates.UpdateType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdatesRepository extends JpaRepository<Updates, Long> {

  @Query("select u from Updates u where u.type = :type")
  List<Updates> findByType(@Param("type") UpdateType type);

  @Query("select u from Updates u where u.context.personnel.id = :id")
  List<Updates> findByPersonnelId(@Param("id") String id);

  @Modifying
  @Query("delete from Updates u where u.context = :context")
  void deleteByContext(@Param("context") UpdateContext context);

  @Query("select u from Updates u where u.type = :type and u.context = :context")
  List<Updates> findByTypeAndContext(
      @Param("type") UpdateType type, @Param("context") UpdateContext context);
}
