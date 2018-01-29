package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.Updates;
import com.hedvig.backoffice.services.updates.UpdateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UpdatesRepository extends JpaRepository<Updates, Long> {

    @Query("select u from Updates u where u.type = :type")
    List<Updates> findByType(@Param("type") UpdateType type);

    @Query("select u from Updates u where u.personnel = :personnel")
    List<Updates> findByPersonnel(@Param("personnel") Personnel personnel);

    @Query("select u from Updates u where u.personnel = :personnel and u.type = :type")
    List<Updates> findByPersonnelAndType(@Param("personnel") Personnel personnel, @Param("type") UpdateType type);

}
