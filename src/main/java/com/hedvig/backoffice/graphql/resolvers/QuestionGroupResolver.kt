package com.hedvig.backoffice.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import com.hedvig.backoffice.graphql.GraphQLConfiguration
import com.hedvig.backoffice.graphql.types.Member
import com.hedvig.backoffice.graphql.types.questions.QuestionGroupType
import com.hedvig.backoffice.services.chat.ChatServiceV2Impl
import com.hedvig.backoffice.services.members.MemberService
import com.hedvig.backoffice.services.personnel.PersonnelService
import graphql.schema.DataFetchingEnvironment
import io.sentry.Sentry
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class QuestionGroupResolver
  (
    private val memberService: MemberService,
  private val personnelService: PersonnelService

) : GraphQLResolver<QuestionGroupType> {
  fun getMember(
    questionGroup: QuestionGroupType,
    env: DataFetchingEnvironment?
  ): Member? {
    return try {
      Member.fromDTO(
        memberService.findByMemberId(
          questionGroup.memberId,
          GraphQLConfiguration.getIdToken(env, personnelService)
        )
      )
    } catch (exception: Exception) {
      logger.error("Unable get to resolve member for Question Group (memberId=${questionGroup.memberId}), exception");
      Sentry.capture(exception)
      null
    }
  }

  companion object {
    private val logger = LoggerFactory.getLogger(ChatServiceV2Impl::class.java)
  }

}
