package com.hedvig.backoffice.graphql.dataloaders

import com.hedvig.backoffice.graphql.types.Claim
import org.dataloader.DataLoader
import org.dataloader.DataLoaderOptions
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ClaimLoader(
    claimBatchLoader: ClaimBatchLoader
) : DataLoader<UUID, Claim>(
    claimBatchLoader,
    DataLoaderOptions.newOptions().setCachingEnabled(false)
)
