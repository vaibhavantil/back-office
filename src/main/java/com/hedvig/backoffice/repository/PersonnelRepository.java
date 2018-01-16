package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.Personnel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface PersonnelRepository extends CrudRepository<Personnel, Long> {

    @Query("select u from Personnel u where u.email = :email")
    Optional<Personnel> findByEmail(@Param("email") String email);

    @Query("select p from Personnel p  where p.id = :id")
    Optional<Personnel> findById(@Param("id") long id);

    @Query("select p from Personnel p")
    Stream<Personnel> all();

}
