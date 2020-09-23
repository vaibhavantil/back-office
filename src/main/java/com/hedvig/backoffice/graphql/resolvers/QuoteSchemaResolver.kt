package com.hedvig.backoffice.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import com.hedvig.backoffice.graphql.types.Quote
import com.hedvig.backoffice.services.underwriter.UnderwriterService

class QuoteSchemaResolver(
  private val underwriterService: UnderwriterService
) : GraphQLResolver<Quote> {

  fun getSchema(quote:Quote)  = underwriterService.getSchemaFromQuote(quote.id)

}
