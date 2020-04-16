package com.hedvig.backoffice.graphql.dataloaders

import com.hedvig.backoffice.graphql.types.account.Account
import com.hedvig.backoffice.services.account.AccountService
import org.dataloader.BatchLoader
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Component
class AccountBatchLoader(private val accountService: AccountService) : BatchLoader<String?, Account> {
  override fun load(memberIds: List<String?>): CompletionStage<List<Account>> {
    return CompletableFuture.supplyAsync {
      accountService.batchFindCurrentBalances(memberIds)
        .map((Account)::from)
    }
  }
}
