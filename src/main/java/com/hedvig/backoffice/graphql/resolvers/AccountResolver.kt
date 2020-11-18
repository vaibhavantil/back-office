package com.hedvig.backoffice.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import com.hedvig.backoffice.graphql.types.account.Account
import com.hedvig.backoffice.graphql.types.account.MonthlyEntry
import com.hedvig.backoffice.services.account.AccountService
import org.springframework.stereotype.Component

@Component
class AccountResolver(
    private val accountService: AccountService
) : GraphQLResolver<Account> {
    fun getMonthlyEntries(account: Account): List<MonthlyEntry> {
        return accountService.getMonthlyEntriesForMember(account.id).map((MonthlyEntry)::from)
    }
}
