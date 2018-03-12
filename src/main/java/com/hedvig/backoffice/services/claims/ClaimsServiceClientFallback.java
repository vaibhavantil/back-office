package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.Claim;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ClaimsServiceClientFallback implements ClaimsServiceClient {

    @Override
    public Map<String, Long> statistics() {
        Map<String, Long> stat = new HashMap<>();
        for (ClaimState state : ClaimState.values()) {
            stat.put(state.name(), 0L);
        }

        return stat;
    }

    @Override
    public List<Claim> listByUserId(String userId) {
        return new ArrayList<>();
    }

}
