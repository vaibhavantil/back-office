package com.hedvig.backoffice.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import com.hedvig.backoffice.graphql.GraphQLConfiguration
import com.hedvig.backoffice.graphql.types.Member
import com.hedvig.backoffice.graphql.types.questions.QuestionGroupType
import com.hedvig.backoffice.services.members.MemberService
import com.hedvig.backoffice.services.personnel.PersonnelService
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class QuestionGroupResolver
  (
  private val memberService: MemberService,
  private val personnelService: PersonnelService
) : GraphQLResolver<QuestionGroupType?> {
  fun getMember(
    QuestionGroup: QuestionGroupType,
    env: DataFetchingEnvironment?
  ): Member? {
    return try {
      Member.fromDTO(
        memberService.findByMemberId(
          QuestionGroup.memberId,
          GraphQLConfiguration.getIdToken(env, personnelService)
        )
      )
    } catch (e: Exception) {
      null
    }
  }

}
