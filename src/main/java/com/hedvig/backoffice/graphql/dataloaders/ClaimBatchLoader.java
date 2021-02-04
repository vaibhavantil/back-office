package com.hedvig.backoffice.graphql.dataloaders;

import com.hedvig.backoffice.graphql.types.Claim;
import com.hedvig.backoffice.services.claims.ClaimsService;
import org.dataloader.BatchLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
public class ClaimBatchLoader implements BatchLoader<UUID, Claim> {

    private final ClaimsService claimsService;

    public ClaimBatchLoader(ClaimsService claimsService) {
        this.claimsService = claimsService;
    }

    @Override
    public CompletionStage<List<Claim>> load(List<UUID> keys) {
        return CompletableFuture
            .supplyAsync(() -> claimsService.getClaimsByIds(keys).stream().map(Claim.Companion::fromDTO)
            .collect(Collectors.toList()));
    }
}
