package com.hedvig.backoffice.graphql.resolvers

import com.fasterxml.jackson.databind.ObjectMapper
import com.hedvig.backoffice.graphql.types.Quote
import com.hedvig.backoffice.graphql.types.QuoteState
import com.hedvig.backoffice.services.underwriter.UnderwriterService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.time.Instant
import java.util.*

class ResolverQuoteSchemaTest {

    lateinit var underwriterService: UnderwriterService

    val objectMapper = ObjectMapper()

    val aRandomSchema = objectMapper.readTree("""
    {
          "${'$'}schema": "http://json-schema.org/draft-07/schema#",
          "title": "Norwegian Travel",
          "type": "object",
          "additionalProperties": false,
          "properties": {
                "coInsured": {
                    "type": "integer",
                    "title": "Co Insured"
              },
              "youth": {
                  "type": "boolean",
                  "title": "Youth"
              }
          }
    }""".trimIndent())

    val randomSchemaData = objectMapper.readTree("""
        {
            "coInsured": 2,
            "youth": true
        }
    """.trimIndent())

    @Before
    fun setup() {
        underwriterService = mockk()
    }

    @Test
    fun `returns a schema`() {

        val quoteId = UUID.randomUUID()
        every { underwriterService.getSchemaByQuoteId(quoteId) } returns aRandomSchema

        val resolver = QuoteResolver(underwriterService)

        val result = resolver.getSchema(
            Quote(
                id = quoteId,
                createdAt = Instant.now(),
                price = null,
                currency = "SEK",
                productType = "APARTMENT",
                state = QuoteState.INCOMPLETE,
                initiatedFrom = "RAPIO",
                attributedTo = "",
                currentInsurer = null,
                startDate = null,
                validity = 123456,
                memberId = null,
                breachedUnderwritingGuidelines = null,
                isComplete = false,
                signedProductId = null,
                originatingProductId = null,
                isReadyToSign = false
            )
        )

        assertThat(result).isEqualTo(aRandomSchema)
    }

    @Test
    fun `returns data for schema`() {

        val quoteId = UUID.randomUUID()
        every { underwriterService.getSchemaDataByQuoteId(quoteId) } returns randomSchemaData

        val resolver = QuoteResolver(underwriterService)

        val result = resolver.getSchemaData(
            Quote(
                id = quoteId,
                createdAt = Instant.now(),
                price = null,
                currency = "SEK",
                productType = "APARTMENT",
                state = QuoteState.INCOMPLETE,
                initiatedFrom = "RAPIO",
                attributedTo = "",
                currentInsurer = null,
                startDate = null,
                validity = 123456,
                memberId = null,
                breachedUnderwritingGuidelines = null,
                isComplete = false,
                signedProductId = null,
                originatingProductId = null,
                isReadyToSign = false
            )
        )

        assertThat(result).isEqualTo(randomSchemaData)
    }
}
