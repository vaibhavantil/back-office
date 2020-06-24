package com.hedvig.backoffice.graphql.types

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.hedvig.backoffice.services.messages.dto.BotMessageDTO
import java.time.Instant

data class ChatMessage(
  val globalId: Long,
  val author: String?,
  val fromId: Long,
  val timestamp: Instant?,
  val messageBodyJsonString: String
) {
  companion object {
    private val jsonMapper = ObjectMapper()
      .registerModule(JavaTimeModule())
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun from(message: BotMessageDTO) = ChatMessage(
      globalId = message.globalId,
      author = message.author,
      fromId = message.header.fromId,
      timestamp = message.timestamp,
      messageBodyJsonString = jsonMapper.writeValueAsString(message.body)
    )
  }
}
