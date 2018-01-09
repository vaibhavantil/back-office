package com.hedvig.backoffice.services.assettracker;

import com.hedvig.backoffice.domain.Asset;
import com.hedvig.common.constant.AssetState;

import java.util.List;

public interface AssetTracker {

    List<Asset> findPendingAssets();
    void updateAssetState(String assetId, AssetState state) throws AssetTrackerException;

}
