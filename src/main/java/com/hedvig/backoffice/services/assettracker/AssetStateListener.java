package com.hedvig.backoffice.services.assettracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssetStateListener {

    private final AssetTracker tracker;

    @Autowired
    public AssetStateListener(AssetTracker tracker) {
        this.tracker = tracker;
    }

    /*@EventHandler
    public void on(ChangeAssetStateEvent event) {
        if (!tracker.updateAssetState(event.getId(), event.getState())) {
            System.out.println();
        }
    }*/

}
