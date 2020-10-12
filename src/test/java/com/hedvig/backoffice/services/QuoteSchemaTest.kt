package com.hedvig.backoffice.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.hedvig.backoffice.services.underwriter.UnderwriterClient
import com.hedvig.backoffice.services.underwriter.UnderwriterServiceImpl
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientResponseException
import java.util.UUID

class QuoteSchemaTest {

  lateinit var underwriterClient: UnderwriterClient
  val objectMapper = ObjectMapper()

  @Before
  fun setup() {
    underwriterClient = mockk()
  }

  val aRandomSchema = """
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
  }
  """.trimIndent()

  @Test
  fun quoteServiceReturnsValidSchema() {

    val schema = objectMapper.readTree(aRandomSchema)
    every { underwriterClient.getSchemaByQuoteId(any()) } returns ResponseEntity.ok(schema)

    val underwriterServiceImpl = UnderwriterServiceImpl(underwriterClient)

    val result = underwriterServiceImpl.getSchemaByQuoteId(UUID.randomUUID())

    assertThat(result).isEqualTo(schema)
  }

  @Test
  fun quoteNotFound_returnsNull() {
    every { underwriterClient.getSchemaByQuoteId(any()) } throws RestClientResponseException(
      "Error",
      404,
      "NOT_FOUND",
      null,
      null,
      null
    )

    val underwriterServiceImpl = UnderwriterServiceImpl(underwriterClient)

    val result = underwriterServiceImpl.getSchemaByQuoteId(UUID.randomUUID())

    assertThat(result).isNull()
  }
}
