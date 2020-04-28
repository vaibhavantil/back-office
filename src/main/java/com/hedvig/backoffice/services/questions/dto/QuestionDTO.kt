package com.hedvig.backoffice.services.questions.dto

import com.hedvig.backoffice.domain.Question
import com.hedvig.backoffice.services.messages.dto.BotMessageDTO
import lombok.AllArgsConstructor
import lombok.Data
import java.time.Instant
import java.util.*

data class QuestionDTO(
  val id: Long,
  val message: BotMessageDTO,
  val date: Long
) {
  companion object {
    fun fromDomain(question: Question): QuestionDTO {
      return QuestionDTO(
        question.id,
        BotMessageDTO.fromJson(question.message),
        Optional.ofNullable(question.date).map { obj: Instant -> obj.toEpochMilli() }.orElse(null)
      )
    }
  }
}
