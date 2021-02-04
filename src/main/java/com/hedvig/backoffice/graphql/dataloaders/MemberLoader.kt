package com.hedvig.backoffice.graphql.dataloaders

import com.hedvig.backoffice.graphql.types.Member
import org.dataloader.DataLoader
import org.dataloader.DataLoaderOptions
import org.springframework.stereotype.Component

@Component
class MemberLoader(
    memberBatchLoader: MemberBatchLoader
) : DataLoader<String, Member>(
    memberBatchLoader,
    DataLoaderOptions.newOptions().setCachingEnabled(false)
)
