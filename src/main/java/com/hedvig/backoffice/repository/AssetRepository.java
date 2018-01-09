package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.Asset;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface AssetRepository extends CrudRepository<Asset, String> {
    @Query("select a from Asset a")
    Stream<Asset> streamAll();
}
