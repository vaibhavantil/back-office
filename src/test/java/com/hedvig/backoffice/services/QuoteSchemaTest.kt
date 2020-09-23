package com.hedvig.backoffice.services

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.hedvig.backoffice.services.underwriter.UnderwriterClient
import com.hedvig.backoffice.services.underwriter.UnderwriterServiceImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.http.ResponseEntity
import java.util.UUID

class QuoteSchemaTest {

  lateinit var underwriterClient: UnderwriterClient
  val objectMapper = ObjectMapper()

  @Before
  fun setup(){
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
              "propertyOrder": 1,
              "type": "integer",
              "title": "Co Insured"
          },
          "youth": {
              "propertyOrder": 2,
              "type": "boolean",
              "title": "Youth"
          }
      }
  }
  """.trimIndent()

  @Test
  fun quoteServiceReturnsValidSchema() {

    val schema = objectMapper.readTree(aRandomSchema)
    every { underwriterClient.getSchemaFromQuote(any()) } returns ResponseEntity.ok(schema)

    val underwriterServiceImpl = UnderwriterServiceImpl(underwriterClient, mockk())

    val result = underwriterServiceImpl.getSchemaFromQuote(UUID.randomUUID())

    assertThat(result).isEqualTo(schema)
  }
}
