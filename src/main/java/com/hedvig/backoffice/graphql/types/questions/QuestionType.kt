package com.hedvig.backoffice.graphql.types.questions

import com.hedvig.backoffice.services.questions.dto.QuestionDTO
import java.time.Instant

data class QuestionType(
  val id: Long,
  val messageJsonString: String,
  val timestamp: Instant
) {
  companion object {
    fun from(question: QuestionDTO) = QuestionType(
      id = question.id,
      messageJsonString = question.message.toJson(),
      timestamp = Instant.ofEpochMilli(question.date)
    )
  }
}
