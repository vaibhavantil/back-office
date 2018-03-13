package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.updates.UpdateType;
import com.hedvig.backoffice.services.updates.UpdatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClaimsUpdates {

    private final ClaimsService claimsService;
    private final UpdatesService updatesService;

    @Autowired
    public ClaimsUpdates(ClaimsService claimsService, UpdatesService updatesService) {
        this.claimsService = claimsService;
        this.updatesService = updatesService;
    }

    @Scheduled(fixedRateString = "${intervals.claims}")
    public void update() {
        updatesService.set(claimsService.totalClaims(), UpdateType.CLAIMS);
    }

}
