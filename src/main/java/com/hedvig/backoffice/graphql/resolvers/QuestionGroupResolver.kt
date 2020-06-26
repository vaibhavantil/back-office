package com.hedvig.backoffice.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import com.hedvig.backoffice.graphql.GraphQLConfiguration
import com.hedvig.backoffice.graphql.types.Member
import com.hedvig.backoffice.graphql.types.Member.Companion.fromDTO
import com.hedvig.backoffice.graphql.types.questions.QuestionGroupType
import com.hedvig.backoffice.services.members.MemberService
import com.hedvig.backoffice.services.personnel.PersonnelService
import com.hedvig.backoffice.util.Gender
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class QuestionGroupResolver
  (
  private val memberService: MemberService,
  private val personnelService: PersonnelService
) :
  GraphQLResolver<QuestionGroupType?> {
  fun getMember(
    schedulerStatus: QuestionGroupType,
    env: DataFetchingEnvironment?
  ): Member {
    val token = GraphQLConfiguration.getIdToken(env, personnelService)
    return try {
      fromDTO(
        memberService.findByMemberId(
          schedulerStatus.memberId,
          token
        )
      )
    } catch (e: Exception) {
      Member(
        schedulerStatus.memberId,
        "UNKNOWN",
        "UNKNOWN",
        "Unknown",
        Gender.OTHER,
        "Unknown",
        "Unknown",
        "Unknown",
        Instant.now(),
        "Unknown",
        "Unknown"
      )
    }
  }

}
