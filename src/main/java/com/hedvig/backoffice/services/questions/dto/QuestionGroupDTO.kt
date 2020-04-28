package com.hedvig.backoffice.services.questions.dto

import com.hedvig.backoffice.domain.QuestionGroup
import com.hedvig.backoffice.web.dto.PersonnelDTO

data class QuestionGroupDTO(
  val id: Long,
  val memberId: String,
  val date: Long?,
  val answerDate: Long?,
  val answer: String?,
  val personnel: PersonnelDTO?,
  val questions: List<QuestionDTO>
) {
  companion object {
    fun fromDomain(group: QuestionGroup): QuestionGroupDTO {
      return QuestionGroupDTO(
        id = group.id,
        memberId = group.subscription.memberId,
        date = group.date?.toEpochMilli(),
        answerDate = group.answerDate?.toEpochMilli(),
        answer = group.answer,
        personnel = if (group.personnel != null) PersonnelDTO.fromDomain(group.personnel) else null,
        questions = group.questions.map { question -> QuestionDTO.fromDomain(question) })
    }
  }
}
