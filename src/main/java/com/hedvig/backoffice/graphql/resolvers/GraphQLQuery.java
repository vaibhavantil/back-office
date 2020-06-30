package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.hedvig.backoffice.graphql.GraphQLConfiguration;
import com.hedvig.backoffice.graphql.GraphQLRequestContext;
import com.hedvig.backoffice.graphql.dataloaders.ClaimLoader;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.*;
import com.hedvig.backoffice.graphql.types.account.SchedulerStatus;
import com.hedvig.backoffice.graphql.types.dashboard.DashboardNumbers;
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory;
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind;
import com.hedvig.backoffice.graphql.types.questions.QuestionGroupType;
import com.hedvig.backoffice.repository.QuestionGroupRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.account.AccountService;
import com.hedvig.backoffice.services.account.ChargeStatus;
import com.hedvig.backoffice.services.account.dto.SchedulerStateDto;
import com.hedvig.backoffice.services.autoAnswerSuggestion.AutoAnswerSuggestionService;
import com.hedvig.backoffice.services.autoAnswerSuggestion.DTOs.SuggestionDTO;
import com.hedvig.backoffice.services.chat.ChatServiceV2;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.itemizer.ItemizerService;
import com.hedvig.backoffice.services.itemizer.dto.CanValuateClaimItem;
import com.hedvig.backoffice.services.itemizer.dto.ClaimItem;
import com.hedvig.backoffice.services.itemizer.dto.ClaimItemValuation;
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

import java.math.BigDecimal;
import java.time.LocalDate;
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
  private final ChatServiceV2 chatServiceV2;
  private final QuestionService questionService;
  private final QuestionGroupRepository questionGroupRepository;
  private final ClaimsService claimsService;

  public GraphQLQuery(
    ProductPricingService productPricingService,
    MemberLoader memberLoader,
    ClaimLoader claimLoader,
    AccountService accountService,
    MemberService memberService,
    TicketService ticketService,
    PersonnelService personnelService,
    AutoAnswerSuggestionService autoAnswerSuggestionService,
    ChatServiceV2 chatServiceV2,
    QuestionService questionService,
    ItemizerService itemizerService,
    QuestionGroupRepository questionGroupRepository,
    ClaimsService claimsService
  ) {
    this.productPricingService = productPricingService;
    this.memberLoader = memberLoader;
    this.claimLoader = claimLoader;
    this.accountService = accountService;
    this.itemizerService = itemizerService;
    this.ticketService = ticketService;
    this.personnelService = personnelService;
    this.autoAnswerSuggestionService = autoAnswerSuggestionService;
    this.chatServiceV2 = chatServiceV2;
    this.questionService = questionService;
    this.questionGroupRepository = questionGroupRepository;
    this.claimsService = claimsService;
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
      log.info("Exception occured when trying to access user getEmail: " + e);
      return null;
    }
  }

  public List<SwitchableSwitcherEmail> switchableSwitcherEmails(DataFetchingEnvironment env) {
    return productPricingService.getSwitchableSwitcherEmails().stream()
      .map(SwitchableSwitcherEmail::from)
      .collect(Collectors.toList());
  }

  public List<ChatMessage> messageHistory(String memberId, DataFetchingEnvironment env) {
    String email = getEmail(env);
    String token = getToken(env);
    return chatServiceV2.fetchMessages(memberId, email, token)
      .stream()
      .map(ChatMessage.Companion::from)
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

  public DashboardNumbers getDashboardNumbers(DataFetchingEnvironment env) {
    String token = getToken(env);
    Long claims = claimsService.totalClaims(token);
    Long questions = questionGroupRepository.notAnsweredCount();
    return new DashboardNumbers(claims, questions);
  }

  private String getToken(DataFetchingEnvironment env) {
    GraphQLRequestContext context = env.getContext();
    return personnelService.getIdToken(context.getUserPrincipal().getName());
  }

  public ClaimItemValuation getClaimItemValuation(
    BigDecimal purchasePrice,
    String itemFamilyId,
    UUID itemTypeId,
    String typeOfContract,
    LocalDate purchaseDate,
    LocalDate baseDate
  ) {
    return itemizerService.getValuation(
      purchasePrice,
      itemFamilyId,
      itemTypeId,
      typeOfContract,
      purchaseDate,
      baseDate
    );
  }

  public CanValuateClaimItem canValuateClaimItem(String typeOfContract, String itemFamilyId, UUID itemTypeId) {
    return itemizerService.canValuateClaimItem(typeOfContract, itemFamilyId, itemTypeId);
  }

  private String getEmail(DataFetchingEnvironment env) {
    try {
      return GraphQLConfiguration.getEmail(env, personnelService);
    } catch (AuthorizationException e) {
      throw new RuntimeException("Failed to get email from GraphQLConfiguration", e);
    }
  }
}
