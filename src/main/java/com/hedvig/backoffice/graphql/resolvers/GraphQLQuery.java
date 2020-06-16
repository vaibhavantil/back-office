package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.hedvig.backoffice.graphql.GraphQLConfiguration;
import com.hedvig.backoffice.graphql.dataloaders.ClaimLoader;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.*;
import com.hedvig.backoffice.graphql.types.account.SchedulerStatus;
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory;
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind;
import com.hedvig.backoffice.graphql.types.questions.QuestionGroupType;
import com.hedvig.backoffice.services.account.AccountService;
import com.hedvig.backoffice.services.account.ChargeStatus;
import com.hedvig.backoffice.services.account.dto.SchedulerStateDto;
import com.hedvig.backoffice.services.autoAnswerSuggestion.AutoAnswerSuggestionService;
import com.hedvig.backoffice.services.autoAnswerSuggestion.DTOs.SuggestionDTO;
import com.hedvig.backoffice.services.itemizer.ItemizerService;
import com.hedvig.backoffice.services.itemizer.dto.ClaimItem;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.product_pricing.PartnerResponseDto;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.dto.PartnerCampaignSearchResponse;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.tickets.TicketService;
import com.hedvig.backoffice.services.tickets.dto.TicketDto;
import com.hedvig.backoffice.services.tickets.dto.TicketHistoryDto;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static graphql.servlet.GraphQLServlet.log;

@Component
public class GraphQLQuery implements GraphQLQueryResolver {

  private final ProductPricingService productPricingService;
  private final MemberLoader memberLoader;
  private final ClaimLoader claimLoader;
  private final AccountService accountService;
  private final ItemizerService itemizerService;
  private final TicketService ticketService;
  private final PersonnelService personnelService;
  private final AutoAnswerSuggestionService autoAnswerSuggestionService;
  private final QuestionService questionService;

  public GraphQLQuery(
    ProductPricingService productPricingService,
    MemberLoader memberLoader,
    ClaimLoader claimLoader,
    AccountService accountService,
    MemberService memberService,
    TicketService ticketService,
    PersonnelService personnelService,
    AutoAnswerSuggestionService autoAnswerSuggestionService,
    QuestionService questionService,
    ItemizerService itemizerService
  ) {
    this.productPricingService = productPricingService;
    this.memberLoader = memberLoader;
    this.claimLoader = claimLoader;
    this.accountService = accountService;
    this.itemizerService = itemizerService;
    this.ticketService = ticketService;
    this.personnelService = personnelService;
    this.autoAnswerSuggestionService = autoAnswerSuggestionService;
    this.questionService = questionService;
  }

  public List<MonthlySubscription> monthlyPayments(YearMonth month) {

    return productPricingService.getMonthlyPayments(month).stream()
      .map(ms -> new MonthlySubscription(ms.getMemberId(), ms.getSubscription()))
      .collect(Collectors.toList());
  }

  public List<SuggestionDTO> getAnswerSuggestion(String question) {
    return autoAnswerSuggestionService.getAnswerSuggestion(question);
  }

  public CompletableFuture<Member> member(String id) {
    return memberLoader.load(id);
  }

  public CompletableFuture<Claim> claim(UUID id) {
    return claimLoader.load(id);
  }

  public List<SchedulerStatus> paymentSchedule(ChargeStatus status) {
    List<SchedulerStateDto> schedulerStateDtos = accountService.subscriptionSchedulesAwaitingApproval(status);

    return schedulerStateDtos
      .stream()
      .map(
        schedulerStateDto -> new SchedulerStatus(
          schedulerStateDto.getStateId(),
          schedulerStateDto.getMemberId(),
          schedulerStateDto.getStatus(),
          schedulerStateDto.getChangedBy(),
          schedulerStateDto.getChangedAt(),
          schedulerStateDto.getAmount(),
          schedulerStateDto.getTransactionId()
        )
      )
      .collect(Collectors.toList());
  }

  public TicketDto ticket(UUID id) {

    return this.ticketService.getTicketById(id);
  }

  public TicketHistoryDto getFullTicketHistory(UUID id) {

    return this.ticketService.getTicketHistory(id);
  }

  public List<TicketDto> tickets(Boolean resolved) {

    return ticketService.getAllTickets(resolved);
  }

  public String me(DataFetchingEnvironment env) {
    try {
      return GraphQLConfiguration.getEmail(env, personnelService);
    } catch (Exception e) {
      log.info("Exception occured when trying to access user email: " + e);
      return null;
    }
  }

  public List<SwitchableSwitcherEmail> switchableSwitcherEmails(DataFetchingEnvironment env) {
    return productPricingService.getSwitchableSwitcherEmails().stream()
      .map(SwitchableSwitcherEmail::from)
      .collect(Collectors.toList());
  }

  public List<QuestionGroupType> questionGroups() {
    return questionService.notAnswered().stream().map(QuestionGroupType.Companion::from).collect(Collectors.toList());
  }

  public List<ItemCategory> itemCategories(ItemCategoryKind kind, String parentId) {
    return itemizerService.getCategories(kind, parentId);
  }

  public List<ClaimItem> claimItems(UUID claimId) {
    return itemizerService.getClaimItems(claimId);
  }

  public List<VoucherCampaign> findPartnerCampaigns(CampaignFilter filter) {
    List<PartnerCampaignSearchResponse> partnerCampaignSearchResponse = filter == null
      ? productPricingService.searchPartnerCampaigns(null, null, null, null)
      : productPricingService.searchPartnerCampaigns(filter.getCode(), filter.getPartnerId(), filter.getActiveFrom(), filter.getActiveTo());

    return partnerCampaignSearchResponse
      .stream()
      .map(VoucherCampaign.Companion::from)
      .collect(Collectors.toList());
  }


  public List<PartnerResponseDto> getPartnerCampaignOwners() {
    return productPricingService.getPartnerCampaignOwners();
  }
}
