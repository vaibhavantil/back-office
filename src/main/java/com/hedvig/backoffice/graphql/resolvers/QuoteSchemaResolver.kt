package com.hedvig.backoffice.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import com.hedvig.backoffice.graphql.types.Quote
import com.hedvig.backoffice.services.underwriter.UnderwriterService
import org.springframework.stereotype.Component

@Component
class QuoteResolver(
    private val underwriterService: UnderwriterService
) : GraphQLResolver<Quote> {

    fun getSchema(quote: Quote) = underwriterService.getSchemaByQuoteId(quote.id)

    fun getSchemaData(quote: Quote) = underwriterService.getSchemaDataByQuoteId(quote.id)
}
