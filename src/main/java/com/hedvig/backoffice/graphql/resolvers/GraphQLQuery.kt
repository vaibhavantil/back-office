package com.hedvig.backoffice.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.hedvig.backoffice.graphql.GraphQLConfiguration
import com.hedvig.backoffice.graphql.dataloaders.ClaimLoader
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader
import com.hedvig.backoffice.graphql.types.Claim
import com.hedvig.backoffice.graphql.types.Member
import com.hedvig.backoffice.graphql.types.MonthlySubscription
import com.hedvig.backoffice.graphql.types.SwitchableSwitcherEmail
import com.hedvig.backoffice.graphql.types.account.SchedulerStatus
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind
import com.hedvig.backoffice.services.account.AccountService
import com.hedvig.backoffice.services.account.ChargeStatus
import com.hedvig.backoffice.services.account.dto.SchedulerStateDto
import com.hedvig.backoffice.services.autoAnswerSuggestion.AutoAnswerSuggestionService
import com.hedvig.backoffice.services.autoAnswerSuggestion.DTOs.SuggestionDTO
import com.hedvig.backoffice.services.itemizer.ItemizerService
import com.hedvig.backoffice.services.itemizer.dto.ClaimItem
import com.hedvig.backoffice.services.members.MemberService
import com.hedvig.backoffice.services.personnel.PersonnelService
import com.hedvig.backoffice.services.product_pricing.ProductPricingService
import com.hedvig.backoffice.services.product_pricing.dto.MonthlySubscriptionDTO
import com.hedvig.backoffice.services.product_pricing.dto.SwitchableSwitcherEmailDTO
import com.hedvig.backoffice.services.tickets.TicketService
import com.hedvig.backoffice.services.tickets.dto.TicketDto
import com.hedvig.backoffice.services.tickets.dto.TicketHistoryDto
import graphql.schema.DataFetchingEnvironment
import graphql.servlet.GraphQLServlet
import org.springframework.stereotype.Component
import java.time.YearMonth
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

@Component
class GraphQLQuery(
  private val productPricingService: ProductPricingService,
  private val memberLoader: MemberLoader,
  private val claimLoader: ClaimLoader,
  private val accountService: AccountService,
  private val memberService: MemberService,
  private val ticketService: TicketService,
  private val personnelService: PersonnelService,
  private val autoAnswerSuggestionService: AutoAnswerSuggestionService,
  private val itemizerService: ItemizerService
) : GraphQLQueryResolver {
  fun monthlyPayments(month: YearMonth?): List<MonthlySubscription> {
    return productPricingService.getMonthlyPayments(month).stream()
      .map { ms: MonthlySubscriptionDTO -> MonthlySubscription(ms.memberId, ms.subscription) }
      .collect(Collectors.toList())
  }

  fun getAnswerSuggestion(question: String?): List<SuggestionDTO> {
    return autoAnswerSuggestionService.getAnswerSuggestion(question)
  }

  fun member(id: String): CompletableFuture<Member> {
    return memberLoader.load(id)
  }

  fun claim(id: UUID): CompletableFuture<Claim> {
    return claimLoader.load(id)
  }

  fun paymentSchedule(status: ChargeStatus?): List<SchedulerStatus> {
    val schedulerStateDtos = accountService.subscriptionSchedulesAwaitingApproval(status)
    return schedulerStateDtos
      .stream()
      .map { schedulerStateDto: SchedulerStateDto ->
        SchedulerStatus(
          schedulerStateDto.stateId,
          schedulerStateDto.memberId,
          schedulerStateDto.status,
          schedulerStateDto.changedBy,
          schedulerStateDto.changedAt,
          schedulerStateDto.amount,
          schedulerStateDto.transactionId
        )
      }
      .collect(Collectors.toList())
  }

  fun ticket(id: UUID?): TicketDto {
    return ticketService.getTicketById(id!!)
  }

  fun getFullTicketHistory(id: UUID?): TicketHistoryDto {
    return ticketService.getTicketHistory(id!!)
  }

  fun tickets(resolved: Boolean?): List<TicketDto> {
    return ticketService.getAllTickets(resolved)
  }

  fun me(env: DataFetchingEnvironment?): String? {
    return try {
      GraphQLConfiguration.getEmail(env, personnelService)
    } catch (e: Exception) {
      GraphQLServlet.log.info("Exception occured when trying to access user email: $e")
      null
    }
  }

  fun switchableSwitcherEmails(env: DataFetchingEnvironment?): List<SwitchableSwitcherEmail> {
    return productPricingService.switchableSwitcherEmails.stream()
      .map { switchableSwitcherEmailDTO: SwitchableSwitcherEmailDTO? -> SwitchableSwitcherEmail.from(switchableSwitcherEmailDTO) }
      .collect(Collectors.toList())
  }

  fun itemCategories(kind: ItemCategoryKind?, parentId: String?): List<ItemCategory> {
    return itemizerService.getCategories(kind!!, parentId)
  }

  fun validateCategoryChain(
    itemFamilyName: String,
    itemTypeName: String?,
    itemCompanyName: String?,
    itemBrandName: String?,
    itemModelName: String?): List<String> {
    return itemizerService.validateCategoryChain(
      itemFamilyName,
      itemTypeName,
      itemCompanyName,
      itemBrandName,
      itemModelName)
  }

  fun claimItems(claimId: UUID): List<ClaimItem> {
    return itemizerService.getClaimItems(claimId)
  }
}
