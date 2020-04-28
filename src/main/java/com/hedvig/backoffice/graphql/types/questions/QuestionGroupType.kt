package com.hedvig.backoffice.graphql.types.questions

import com.hedvig.backoffice.domain.QuestionGroup
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO

data class QuestionGroupType(
  val id: Long,
  val memberId: String,
  val questions: List<QuestionType>
) {
  companion object {
    fun from(questionGroup: QuestionGroupDTO) = QuestionGroupType(
      id = questionGroup.id,
      memberId = questionGroup.memberId,
      questions = questionGroup.questions.map((QuestionType)::from)
    )
  }
}
