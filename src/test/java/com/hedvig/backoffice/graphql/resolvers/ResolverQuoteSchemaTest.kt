package com.hedvig.backoffice.graphql.resolvers

import com.fasterxml.jackson.databind.ObjectMapper
import com.hedvig.backoffice.graphql.types.ProductType
import com.hedvig.backoffice.graphql.types.Quote
import com.hedvig.backoffice.graphql.types.QuoteData
import com.hedvig.backoffice.graphql.types.QuoteState
import com.hedvig.backoffice.services.underwriter.UnderwriterService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.time.Instant
import java.util.UUID

class ResolverQuoteSchemaTest {

  lateinit var underwriterService: UnderwriterService

  @Before
  fun setup() {
    underwriterService = mockk()
  }

  val aRandomSchema = ObjectMapper().readTree("{}")

  @Test
  fun first_test() {

    val quoteId = UUID.randomUUID()
    every { underwriterService.getSchemaFromQuote(quoteId) } returns aRandomSchema

    val resolver = QuoteSchemaResolver(underwriterService)

    val result = resolver.getSchema(
      Quote(
        quoteId,
        Instant.now(),
        null,
        ProductType.APARTMENT,
        QuoteState.INCOMPLETE,
        "RAPIO",
        "",
        QuoteData.ApartmentQuoteData(UUID.randomUUID()),
        null,
        null,
        123456,
        null,
        null,
        false,
        null,
        null,
        false
      )
    )

    assertThat(result).isEqualTo(aRandomSchema)
  }
}
