package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.Personnel;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonnelRepository extends CrudRepository<Personnel, Long> {

  @Query("select p from Personnel p  where p.id = :id")
  Optional<Personnel> findById(@Param("id") String id);

  @Query("select p from Personnel p")
  Stream<Personnel> all();
}
