package com.hedvig.backoffice.graphql.dataloaders

import com.hedvig.backoffice.graphql.types.account.Account
import org.dataloader.DataLoader
import org.dataloader.DataLoaderOptions
import org.springframework.stereotype.Component

@Component
class AccountLoader(
    accountBatchLoader: AccountBatchLoader
) : DataLoader<String, Account>(
    accountBatchLoader,
    DataLoaderOptions.newOptions().setCachingEnabled(false)
)
