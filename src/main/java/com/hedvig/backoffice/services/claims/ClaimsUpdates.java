package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.settings.SystemSettingsService;
import com.hedvig.backoffice.services.updates.UpdateType;
import com.hedvig.backoffice.services.updates.UpdatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClaimsUpdates {

    private final ClaimsService claimsService;
    private final UpdatesService updatesService;
    private final SystemSettingsService settingsService;

    @Autowired
    public ClaimsUpdates(ClaimsService claimsService, UpdatesService updatesService, SystemSettingsService settingsService) {
        this.claimsService = claimsService;
        this.updatesService = updatesService;
        this.settingsService = settingsService;
    }

    @Scheduled(fixedRateString = "${intervals.claims}")
    public void update() {
        updatesService.set(claimsService.totalClaims(settingsService.getInternalAccessToken()), UpdateType.CLAIMS);
    }

}
