package com.hedvig.backoffice.graphql.dataloaders;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import com.hedvig.backoffice.graphql.types.Claim;
import com.hedvig.backoffice.services.claims.ClaimsService;
import org.dataloader.BatchLoader;
import org.springframework.stereotype.Component;

@Component
public class ClaimBatchLoader implements BatchLoader<UUID, Claim> {

  private final ClaimsService claimsService;

  public ClaimBatchLoader(ClaimsService claimsService) {
    this.claimsService = claimsService;
  }

  @Override
  public CompletionStage<List<Claim>> load(List<UUID> keys) {
    return CompletableFuture.supplyAsync(() -> {
      return claimsService.getClaimsByIds(keys).stream().map(c -> Claim.fromDTO(c))
          .collect(Collectors.toList());
    });
  }
}
