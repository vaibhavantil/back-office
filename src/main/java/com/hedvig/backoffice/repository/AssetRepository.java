package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.Asset;
import com.hedvig.common.constant.AssetState;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {
  @Query("select a from Asset a")
  Stream<Asset> streamAll();

  @Query("select a from Asset a where a.id in (:ids)")
  List<Asset> findAssetsById(@Param("ids") List<String> ids);

  Long countAllByState(@Param("state") AssetState state);
}
