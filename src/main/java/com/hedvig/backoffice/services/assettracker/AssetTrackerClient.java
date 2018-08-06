package com.hedvig.backoffice.services.assettracker;

import com.hedvig.backoffice.domain.Asset;
import java.util.List;

public interface AssetTrackerClient {

  List<Asset> findPendingAssets();

  void updateAsset(Asset asset) throws AssetTrackerException;
}
