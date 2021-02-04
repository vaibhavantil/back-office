package com.hedvig.backoffice.graphql.dataloaders

import com.hedvig.backoffice.graphql.types.Claim
import com.hedvig.backoffice.services.claims.ClaimsService
import org.dataloader.BatchLoader
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Component
class ClaimBatchLoader(
    private val claimsService: ClaimsService
) : BatchLoader<UUID, Claim> {
    override fun load(keys: List<UUID?>): CompletionStage<List<Claim>> = CompletableFuture.supplyAsync {
        claimsService.getClaimsByIds(keys).map((Claim)::fromDTO)
    }
}
