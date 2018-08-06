package com.hedvig.backoffice.services.assettracker;

import com.hedvig.backoffice.web.dto.assets.AssetDTO;
import com.hedvig.common.constant.AssetState;
import java.util.List;

public interface AssetTrackerService {

  void loadPendingAssetsFromTracker();

  List<AssetDTO> findAll();

  AssetDTO find(String assetId) throws AssetNotFoundException;

  void changeAssetState(String assetId, AssetState state)
      throws AssetNotFoundException, AssetTrackerException;
}
