package com.hedvig.backoffice.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader
import com.hedvig.backoffice.graphql.types.Member
import com.hedvig.backoffice.graphql.types.Quote
import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class QuoteResolver(private val memberLoader: MemberLoader) : GraphQLResolver<Quote> {
  fun getMember(quote: Quote): CompletableFuture<Member> =
    memberLoader.load(quote.memberId)
}
