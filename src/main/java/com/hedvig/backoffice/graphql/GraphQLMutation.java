package com.hedvig.backoffice.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.google.common.collect.ImmutableMap;
import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.graphql.dataloaders.ClaimLoader;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Claim;
import com.hedvig.backoffice.graphql.types.*;
import com.hedvig.backoffice.graphql.types.account.AccountEntryInput;
import com.hedvig.backoffice.graphql.types.claims.SetContractForClaim;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.account.AccountService;
import com.hedvig.backoffice.services.account.dto.ApproveChargeRequestDto;
import com.hedvig.backoffice.services.apigateway.ApiGatewayService;
import com.hedvig.backoffice.services.chat.ChatServiceV2;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.dto.ClaimPayment;
import com.hedvig.backoffice.services.claims.dto.ClaimPaymentType;
import com.hedvig.backoffice.services.claims.dto.*;
import com.hedvig.backoffice.services.itemizer.ItemizerService;
import com.hedvig.backoffice.services.itemizer.dto.request.*;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.priceEngine.PriceEngineService;
import com.hedvig.backoffice.services.priceEngine.dto.CreateNorwegianGripenRequest;
import com.hedvig.backoffice.services.product_pricing.dto.ManualRedeemCampaignRequest;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.dto.AssignVoucherPercentageDiscountRequest;
import com.hedvig.backoffice.services.product_pricing.dto.ManualRedeemEnableReferralsCampaignRequest;
import com.hedvig.backoffice.services.product_pricing.dto.ManualUnRedeemCampaignRequest;
import com.hedvig.backoffice.services.product_pricing.dto.contract.*;
import com.hedvig.backoffice.services.questions.QuestionNotFoundException;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.tickets.TicketService;
import com.hedvig.backoffice.services.tickets.dto.CreateTicketDto;
import com.hedvig.backoffice.services.tickets.dto.TicketStatus;
import com.hedvig.backoffice.services.underwriter.UnderwriterService;
import com.hedvig.backoffice.services.underwriter.dtos.*;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.execution.DataFetcherResult;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import jersey.repackaged.com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.hedvig.backoffice.util.TzHelper.SWEDEN_TZ;

@Component
@Slf4j
public class GraphQLMutation implements GraphQLMutationResolver {

  private final MemberLoader memberLoader;
  private final PaymentService paymentService;
  private final PersonnelService personnelService;
  private final ClaimLoader claimLoader;
  private final ClaimsService claimsService;
  private final AccountService accountService;
  private final TicketService ticketService;
  private final QuestionService questionsService;
  private final MemberService memberService;
  private final UnderwriterService underwriterService;
  private final ProductPricingService productPricingService;
  private final PriceEngineService priceEngineService;
  private final ChatServiceV2 chatServiceV2;
  private final ItemizerService itemizerService;
  private final ApiGatewayService apiGatewayService;

  public GraphQLMutation(
    PaymentService paymentService,
    PersonnelService personnelService,
    MemberLoader memberLoader,
    ClaimLoader claimLoader,
    ClaimsService claimsService,
    AccountService accountService,
    TicketService ticketService,
    QuestionService questionsService,
    MemberService memberService,
    UnderwriterService underwriterService,
    ProductPricingService productPricingService,
    PriceEngineService priceEngineService,
    ChatServiceV2 chatServiceV2,
    ItemizerService itemizerService,
    final ApiGatewayService apiGatewayService
  ) {
    this.paymentService = paymentService;
    this.personnelService = personnelService;
    this.memberLoader = memberLoader;
    this.claimLoader = claimLoader;
    this.claimsService = claimsService;
    this.accountService = accountService;
    this.ticketService = ticketService;
    this.questionsService = questionsService;
    this.memberService = memberService;
    this.underwriterService = underwriterService;
    this.productPricingService = productPricingService;
    this.priceEngineService = priceEngineService;
    this.chatServiceV2 = chatServiceV2;
    this.itemizerService = itemizerService;
    this.apiGatewayService = apiGatewayService;
  }

  public CompletableFuture<Member> chargeMember(
    String id,
    MonetaryAmount amount,
    DataFetchingEnvironment env
  ) throws AuthorizationException {
    String email = GraphQLConfiguration.getEmail(env, personnelService);
    log.info("Personnel with email '{}' attempting to charge member '{}' the amount '{}'",
      email, id, amount.toString());
    paymentService.chargeMember(id, amount, email);
    return memberLoader.load(id);
  }

  public CompletableFuture<Member> addAccountEntryToMember(
    final String memberId,
    final AccountEntryInput accountEntryInput,
    final DataFetchingEnvironment env
  ) throws AuthorizationException {
    accountService.addAccountEntry(memberId, accountEntryInput, GraphQLConfiguration.getEmail(env, personnelService));
    return memberLoader.load(memberId);
  }

  public CompletableFuture<Member> backfillSubscriptions(
    final String memberId,
    final DataFetchingEnvironment env
  ) throws AuthorizationException {
    accountService.backfillSubscriptions(memberId, GraphQLConfiguration.getEmail(env, personnelService));
    return memberLoader.load(memberId);
  }

  public UUID createClaim(
    String memberId,
    LocalDateTime date,
    ClaimSource source,
    DataFetchingEnvironment env
  ) {
    GraphQLRequestContext context = env.getContext();
    String token = personnelService.getIdToken(context.getUserPrincipal().getName());
    return claimsService.createClaim(
      new CreateBackofficeClaimDTO(memberId, date.atZone(SWEDEN_TZ).toInstant(), source), token);
  }

  public Boolean approveMemberCharge(
    List<MemberChargeApproval> memberChargeApprovals,
    DataFetchingEnvironment env
  ) throws AuthorizationException {
    accountService.addApprovedSubscriptions(
      memberChargeApprovals
        .stream()
        .map(ApproveChargeRequestDto::from)
        .collect(Collectors.toList()),
      GraphQLConfiguration.getEmail(env, personnelService)
    );
    return true;
  }

  public PaymentCompletionResponse createPaymentCompletionLink(final String memberId) {
    return new PaymentCompletionResponse(
      apiGatewayService.generatePaymentsLink(memberId).getUrl()
    );
  }

  public CompletableFuture<Claim> updateClaimState(
    UUID id,
    ClaimState claimState,
    DataFetchingEnvironment env
  ) throws AuthorizationException {
    log.info("Personnel with email '{}' updating claim status",
      GraphQLConfiguration.getEmail(env, personnelService));
    val stateChangeDto = new ClaimStateUpdate();
    stateChangeDto.setClaimID(id.toString());
    stateChangeDto
      .setState(com.hedvig.backoffice.services.claims.ClaimState.valueOf(claimState.toString()));
    claimsService.changeState(stateChangeDto,
      GraphQLConfiguration.getIdToken(env, personnelService));
    return claimLoader.load(id);
  }

  public CompletableFuture<Claim> addClaimNote(
    UUID id,
    ClaimNoteInput input,
    DataFetchingEnvironment env
  ) throws AuthorizationException {
    log.info("Personnell with email '{}' adding claim note",
      GraphQLConfiguration.getEmail(env, personnelService));
    val noteDto = new com.hedvig.backoffice.services.claims.dto.ClaimNote();
    noteDto.setText(input.getText());
    noteDto.setClaimID(id.toString());
    claimsService.addNote(noteDto, GraphQLConfiguration.getIdToken(env, personnelService));
    return claimLoader.load(id);
  }

  public CompletableFuture<Claim> updateReserve(
    UUID id,
    MonetaryAmount amount,
    DataFetchingEnvironment env
  ) throws AuthorizationException {
    log.debug("Personnell with email '{}' updating reserve",
      GraphQLConfiguration.getEmail(env, personnelService));
    val reserveRequest = new ClaimReserveUpdate();
    reserveRequest.setClaimID(id.toString());
    reserveRequest.setAmount(BigDecimal.valueOf(amount.getNumber().doubleValueExact()));
    claimsService.changeReserve(reserveRequest, GraphQLConfiguration.getIdToken(env, personnelService));
    return claimLoader.load(id);
  }

  public CompletableFuture<DataFetcherResult<Claim>> createClaimPayment(
    UUID id,
    ClaimPaymentInput payment,
    DataFetchingEnvironment env
  ) throws AuthorizationException {
    log.info("Personnel with email '{}'' adding claim payment",
      GraphQLConfiguration.getEmail(env, personnelService));
    val claim =
      claimsService.find(id.toString(), GraphQLConfiguration.getIdToken(env, personnelService));
    val memberId = claim.getUserId();
    val paymentDto = new ClaimPayment();
    paymentDto.setAmount(BigDecimal.valueOf(payment.getAmount().getNumber().doubleValueExact()));
    paymentDto.setDeductible(BigDecimal.valueOf(payment.getDeductible().getNumber().doubleValueExact()));
    paymentDto.setNote(payment.getNote());
    paymentDto.setExGratia(payment.getExGratia());
    paymentDto.setType(ClaimPaymentType.valueOf(payment.getType().toString()));
    paymentDto.setClaimID(id.toString());
    paymentDto.setHandlerReference(GraphQLConfiguration.getEmail(env, personnelService));
    paymentDto.setSanctionListSkipped(payment.isSanctionListSkipped());
    switch (claimsService.addPayment(memberId, paymentDto,
      GraphQLConfiguration.getIdToken(env, personnelService))) {
      case SUCCESSFUL: {
        return claimLoader.load(id)
          .thenApply(c -> new DataFetcherResult<>(c, Collections.EMPTY_LIST));
      }
      case FORBIDDEN:
      case FAILED: {
        return CompletableFuture.completedFuture(
          new DataFetcherResult<>(null,
            Lists.newArrayList(new GraphQLError() { //TODO: fix that error

              @Override
              public String getMessage() {
                return "potentially sanctioned";
              }

              @Override
              public List<SourceLocation> getLocations() {
                return null;
              }

              @Override
              public ErrorType getErrorType() {
                return null;
              }

              @Override
              public Map<String, Object> getExtensions() {
                return ImmutableMap.of("code", 403);
              }
            })));
      }

      default: {
        throw new RuntimeException(
          "ClaimsService.addPayment returned nothing, this code should be unreachable");
      }
    }
  }

  public CompletableFuture<Claim> setClaimType(
    UUID id,
    ClaimTypes type,
    DataFetchingEnvironment env
  ) throws AuthorizationException {
    log.info("Personnel with email '{}' setting claim type",
      GraphQLConfiguration.getEmail(env, personnelService));
    val claimTypeDto = new ClaimTypeUpdate();
    claimTypeDto.setClaimID(id.toString());
    claimTypeDto.setType(Util.claimServiceType(type));
    claimsService.changeType(claimTypeDto, GraphQLConfiguration.getIdToken(env, personnelService));

    return claimLoader.load(id);
  }

  public CompletableFuture<Claim> setCoveringEmployee(
    UUID id,
    boolean coveringEmployee,
    DataFetchingEnvironment env
  ) throws AuthorizationException {
    EmployeeClaimRequestDTO request = new EmployeeClaimRequestDTO(id.toString(), coveringEmployee);
    claimsService.markEmployeeClaim(request, GraphQLConfiguration.getIdToken(env, personnelService));
    return claimLoader.load(id);
  }

  public CompletableFuture<Claim> setClaimInformation(
    UUID id,
    ClaimInformationInput claimInformationInput,
    DataFetchingEnvironment env
  ) throws AuthorizationException {
    log.info("Personnel with email '{}' updating claim information",
      GraphQLConfiguration.getEmail(env, personnelService));
    val claim =
      claimsService.find(id.toString(), GraphQLConfiguration.getIdToken(env, personnelService));

    val claimData = claim.getData();

    val now = LocalDateTime.now();

    val groupedClaimData = claimData.stream().collect(Collectors.groupingBy(ClaimData::getName));

    val prevLocation = groupedClaimData.getOrDefault("PLACE", Collections.emptyList()).stream()
      .sorted(Util.sortedByDateDescComparator).findFirst();
    if (claimInformationInput.getLocation() != null && !(prevLocation.isPresent()
      && prevLocation.get().getValue().equals(claimInformationInput.getLocation()))) {
      val data = new ClaimData();
      data.setClaimID(id.toString());
      data.setName("PLACE");
      data.setType("TEXT");
      data.setTitle("Place");
      data.setDate(now);
      data.setValue(claimInformationInput.getLocation());
      claimsService.addData(data, GraphQLConfiguration.getIdToken(env, personnelService));
    }

    val prevDate = groupedClaimData.getOrDefault("DATE", Collections.emptyList()).stream()
      .sorted(Util.sortedByDateDescComparator).findFirst();
    log.info("previous Date: {}, new Date: {}",
      prevDate.orElseGet(() -> new ClaimData()).getValue(), claimInformationInput.getDate());
    if (claimInformationInput.getDate() != null && !(prevDate.isPresent()
      && prevDate.get().getValue().equals(claimInformationInput.getDate().toString()))) {
      val data = new ClaimData();
      data.setClaimID(id.toString());
      data.setType("DATE");
      data.setName("DATE");
      data.setTitle("Date");
      data.setDate(now);
      data.setValue(claimInformationInput.getDate().atTime(LocalTime.of(10, 0, 0)).toString());
      claimsService.addData(data, GraphQLConfiguration.getIdToken(env, personnelService));
    }

    val prevItem = groupedClaimData.getOrDefault("ITEM", Collections.emptyList()).stream()
      .sorted(Util.sortedByDateDescComparator).findFirst();
    if (claimInformationInput.getItem() != null && !(prevItem.isPresent()
      && prevItem.get().getValue().equals(claimInformationInput.getItem()))) {
      val data = new ClaimData();
      data.setClaimID(id.toString());
      data.setName("ITEM");
      data.setType("ASSET");
      data.setTitle("Item");
      data.setDate(now);
      data.setValue(claimInformationInput.getItem());
      claimsService.addData(data, GraphQLConfiguration.getIdToken(env, personnelService));
    }

    val prevPoliceReport = groupedClaimData.getOrDefault("POLICE_REPORT", Collections.emptyList())
      .stream().sorted(Util.sortedByDateDescComparator).findFirst();
    if (claimInformationInput.getPoliceReport() != null && !(prevPoliceReport.isPresent()
      && prevPoliceReport.get().getValue().equals(claimInformationInput.getPoliceReport()))) {
      val data = new ClaimData();
      data.setClaimID(id.toString());
      data.setName("POLICE_REPORT");
      data.setType("FILE");
      data.setTitle("Police report");
      data.setDate(now);
      data.setValue(claimInformationInput.getPoliceReport());
      claimsService.addData(data, GraphQLConfiguration.getIdToken(env, personnelService));
    }

    val prevReceipt = groupedClaimData.getOrDefault("RECEIPT", Collections.emptyList()).stream()
      .sorted(Util.sortedByDateDescComparator).findFirst();
    if (claimInformationInput.getReceipt() != null && !(prevReceipt.isPresent()
      && prevReceipt.get().getValue().equals(claimInformationInput.getReceipt()))) {
      val data = new ClaimData();
      data.setClaimID(id.toString());
      data.setName("RECEIPT");
      data.setType("FILE");
      data.setTitle("Receipt");
      data.setDate(now);
      data.setValue(claimInformationInput.getReceipt());
      claimsService.addData(data, GraphQLConfiguration.getIdToken(env, personnelService));
    }

    val prevTicket = groupedClaimData.getOrDefault("TICKET", Collections.emptyList()).stream()
      .sorted(Util.sortedByDateDescComparator).findFirst();
    if (claimInformationInput.getTicket() != null && !(prevTicket.isPresent()
      && prevTicket.get().getValue().equals(claimInformationInput.getTicket()))) {
      val data = new ClaimData();
      data.setClaimID(id.toString());
      data.setName("TICKET");
      data.setType("TICKET");
      data.setTitle("TicketDto");
      data.setDate(now);
      data.setValue(claimInformationInput.getTicket());
      claimsService.addData(data, GraphQLConfiguration.getIdToken(env, personnelService));
    }

    return claimLoader.load(id);
  }

  public Quote activateQuote(
    final UUID id,
    final LocalDate activationDate,
    @Nullable final LocalDate terminationDate
  ) {
    return Quote.from(underwriterService.activateQuote(id, activationDate, terminationDate));
  }

  public Quote addAgreementFromQuote(
    final UUID id,
    @Nullable final UUID contractId,
    @Nullable final LocalDate activeFrom,
    @Nullable final LocalDate activeTo,
    @Nullable final LocalDate previousAgreementActiveTo
  ) {
    return Quote.from(underwriterService.addAgreementFromQuote(id, contractId, activeFrom, activeTo, previousAgreementActiveTo));
  }

  public Quote createQuoteFromProduct(final String memberId, final QuoteFromProductInput quoteData,
                                      final DataFetchingEnvironment env) {
    final UUID createdQuoteId = underwriterService.createAndCompleteQuote(memberId, CreateQuoteFromProductDto.from(quoteData), getEmail(env)).getId();
    return Quote.from(underwriterService.getQuote(createdQuoteId));
  }

  public Quote createQuoteFromAgreement(
    final UUID agreementId,
    final String memberId,
    final DataFetchingEnvironment env
  ) {
    final UUID createQuoteId = underwriterService.createQuoteFromAgreement(
      new QuoteFromAgreementRequestDto(
        agreementId,
        memberId,
        getEmail(env)
      )
    ).getId();
    return Quote.from(underwriterService.getQuote(createQuoteId));
  }

  public Quote updateQuote(
    final UUID quoteId,
    final QuoteInput quoteInput,
    final boolean bypassUnderwritingGuidelines,
    final DataFetchingEnvironment env
  ) {
    return Quote.from(underwriterService.updateQuote(
      quoteId,
      QuoteInputDto.from(quoteInput),
      bypassUnderwritingGuidelines ? getEmail(env) : null
    ));
  }

  Quote createQuoteForNewContract(
    final String memberId,
    final QuoteInput quoteInput,
    final boolean bypassUnderwritingGuidelines,
    final DataFetchingEnvironment env
  ) {
    final String bypassUnderwritingGuidelinesFrom = bypassUnderwritingGuidelines ? getEmail(env) : null;

    final UUID createQuoteId = underwriterService.createQuoteForNewContract(
      new QuoteForNewContractRequestDto(
        QuoteRequestDto.Companion.from(quoteInput, memberId, bypassUnderwritingGuidelinesFrom),
        bypassUnderwritingGuidelinesFrom
      )
    ).getId();
    return Quote.from(underwriterService.getQuote(createQuoteId));
  }

  Quote signQuoteForNewContract(
    final UUID quoteId,
    final LocalDate activationDate,
    final DataFetchingEnvironment env
  ) {
    underwriterService.signQuoteForNewContract(
      quoteId,
      new SignQuoteFromHopeRequestDto(
        activationDate,
        getToken(env)
      )
    );
    return Quote.from(underwriterService.getQuote(quoteId));
  }

  UUID createTicket(
    TicketInput ticket,
    DataFetchingEnvironment env
  ) {
    String createdBy = getEmail(env);
    CreateTicketDto ticketDto = CreateTicketDto.Companion.from(ticket, createdBy);
    return this.ticketService.createTicket(ticketDto, createdBy);
  }

  UUID changeTicketDescription(
    UUID ticketId,
    String newDescription,
    DataFetchingEnvironment env
  ) {
    String modifiedBy = getEmail(env);
    this.ticketService.changeDescription(ticketId, newDescription, modifiedBy);
    return ticketId;
  }

  UUID assignTicketToTeamMember(
    UUID ticketId,
    String teamMemberId,
    DataFetchingEnvironment env
  ) {
    String modifiedBy = getEmail(env);
    this.ticketService.changeAssignedTo(ticketId, teamMemberId, modifiedBy);
    return ticketId;
  }

  UUID changeTicketStatus(
    UUID ticketId,
    TicketStatus newStatus,
    DataFetchingEnvironment env
  ) {
    String modifiedBy = getEmail(env);
    this.ticketService.changeStatus(ticketId, newStatus, modifiedBy);
    return ticketId;
  }

  UUID changeTicketReminder(
    UUID ticketId,
    RemindNotification newReminder,
    DataFetchingEnvironment env
  ) {
    String modifiedBy = getEmail(env);
    this.ticketService.changeReminder(ticketId, newReminder, modifiedBy);
    return ticketId;
  }

  UUID changeTicketPriority(
    UUID ticketId,
    float newPriority,
    DataFetchingEnvironment env
  ) {
    String modifiedBy = getEmail(env);
    this.ticketService.changePriority(ticketId, newPriority, modifiedBy);
    return ticketId;
  }

  public Boolean whitelistMember(
    String memberId,
    DataFetchingEnvironment env
  ) throws AuthorizationException {
    String email = getEmail(env);
    memberService.whitelistMember(memberId, email);
    return true;
  }

  public Boolean markClaimFileAsDeleted(
    String claimId,
    UUID claimFileId,
    DataFetchingEnvironment env
  ) {
    String email = getEmail(env);
    MarkClaimFileAsDeletedDTO deletedBy = new MarkClaimFileAsDeletedDTO(email);
    claimsService.markClaimFileAsDeleted(claimId, claimFileId, deletedBy);
    return true;
  }

  public String setClaimFileCategory(
    String claimId,
    UUID claimFileId,
    String category,
    DataFetchingEnvironment env) {
    ClaimFileCategoryDTO claimFileCategoryDTO = new ClaimFileCategoryDTO(category);
    claimsService.setClaimFileCategory(claimId, claimFileId, claimFileCategoryDTO);
    return category;
  }

  public Boolean markSwitchableSwitcherEmailAsReminded(final UUID emailId) {
    productPricingService.markSwitchableSwitcherEmailAsReminded(emailId);
    return true;
  }

  public Contract activatePendingAgreement(final UUID contractId, final ActivatePendingAgreementRequest request, DataFetchingEnvironment env) {
    productPricingService.activatePendingAgreement(contractId, request, getToken(env));
    return productPricingService.getContractById(contractId);
  }

  public Contract terminateContract(final UUID contractId, final TerminateContractRequest request, DataFetchingEnvironment env) {
    productPricingService.terminateContract(contractId, request, getToken(env));
    return productPricingService.getContractById(contractId);
  }

  public Contract changeTerminationDate(final UUID contractId, final ChangeTerminationDateRequest request, DataFetchingEnvironment env) {
    productPricingService.changeTerminationDate(contractId, request, getToken(env));
    return productPricingService.getContractById(contractId);
  }

  public Contract revertTermination(final UUID contractId, DataFetchingEnvironment env) {
    productPricingService.revertTermination(contractId, getToken(env));
    return productPricingService.getContractById(contractId);
  }

  public Boolean createNorwegianGripenPriceEngine(
    final CreateNorwegianGripenRequest request, DataFetchingEnvironment env) {
    priceEngineService.createNorwegianGripenPriceEngine(request, getToken(env));
    return true;
  }

  public Boolean addNorwegianPostalCodes(final String postalCodesString, DataFetchingEnvironment env) {
    priceEngineService.addNorwegianPostalCoodes(postalCodesString);
    return true;
  }

  public UUID changeToDate(final UUID agreementId, final ChangeToDateOnAgreementRequest request, DataFetchingEnvironment env) {
    productPricingService.changeToDate(
      agreementId,
      request,
      getToken(env)
    );
    return agreementId;
  }

  public UUID changeFromDate(final UUID agreementId, final ChangeFromDateOnAgreementRequest request, DataFetchingEnvironment env) {
    productPricingService.changeFromDate(
      agreementId,
      request,
      getToken(env)
    );
    return agreementId;
  }

  public UUID regenerateCertificate(final UUID agreementId, DataFetchingEnvironment env) {
    productPricingService.regenerateCertificate(
      agreementId,
      getToken(env)
    );
    return agreementId;
  }

  public SendMessageResponse sendMessage(SendMessageInput input, DataFetchingEnvironment env) {
    return SendMessageResponse.Companion.from(
      chatServiceV2.sendMessage(
        input.getMemberId(),
        input.getMessage(),
        input.getForceSendMessage(),
        getEmail(env),
        getToken(env)
      )
    );
  }

  public Boolean markQuestionAsResolved(final String memberId, DataFetchingEnvironment env) {
    Personnel personnel = getPersonnel(env);
    try {
      questionsService.done(memberId, personnel);
    } catch (QuestionNotFoundException exception) {
      log.error("Question not found when marking as done for memberId=" + memberId, exception);
      throw new RuntimeException("Unable to mark question as done for memberId=" + memberId);
    }
    return true;
  }

  public Boolean answerQuestion(final String memberId, final String answer, DataFetchingEnvironment env) {
    Personnel personnel = getPersonnel(env);
    try {
      questionsService.answer(memberId, answer, personnel);
    } catch (QuestionNotFoundException exception) {
      log.error("Question not found when answering for memberId=" + memberId, exception);
      throw new RuntimeException("Unable to answer question for memberId=" + memberId);
    }
    return true;
  }

  public UUID upsertItemCompany(final UpsertItemCompanyRequest request, DataFetchingEnvironment env) {
    return itemizerService.upsertItemCompany(request, getEmail(env));
  }

  public UUID upsertItemType(final UpsertItemTypeRequest request, DataFetchingEnvironment env) {
    return itemizerService.upsertItemType(request, getEmail(env));
  }

  public UUID upsertItemBrand(final UpsertItemBrandRequest request, DataFetchingEnvironment env) {
    return itemizerService.upsertItemBrand(request, getEmail(env));
  }

  public UUID upsertItemModel(final UpsertItemModelRequest request, DataFetchingEnvironment env) {
    return itemizerService.upsertItemModel(request, getEmail(env));
  }

  public UUID upsertClaimItem(final UpsertClaimItemRequest request, DataFetchingEnvironment env) {
    return itemizerService.upsertClaimItem(request, getEmail(env));
  }

  public UUID deleteClaimItem(final UUID claimItemId, DataFetchingEnvironment env) {
    return itemizerService.deleteClaimItem(claimItemId, getEmail(env));
  }

  public UUID upsertValuationRule(final UpsertValuationRuleRequest request, DataFetchingEnvironment env) {
    return itemizerService.upsertValuationRule(request, getEmail(env));
  }

  public List<Boolean> insertItemCategories(final InsertItemCategoriesRequest request, DataFetchingEnvironment env) {
    return itemizerService.insertItemCategories(request, getEmail(env));
  }

  public List<Boolean> insertValuationRules(final InsertValuationRulesRequest request, DataFetchingEnvironment env) {
    return itemizerService.insertValuationRules(request, getEmail(env));
  }

  public Boolean assignCampaignToPartnerPercentageDiscount(AssignVoucherPercentageDiscount request, DataFetchingEnvironment env) {
    productPricingService.assignCampaignToPartnerPercentageDiscount(
      AssignVoucherPercentageDiscountRequest.Companion.from(request)
    );
    return true;
  }

  public Boolean setContractForClaim(SetContractForClaim request) {
    claimsService.setContractForClaim(request);
    return true;
  }

  public Boolean manualRedeemCampaign(final String memberId, final ManualRedeemCampaignRequest request) {
    productPricingService.manualRedeemCampaign(memberId, request);
    return true;
  }

  public Boolean manualUnRedeemCampaign(final String memberId, final ManualUnRedeemCampaignRequest request) {
    productPricingService.manualUnRedeemCampaign(memberId, request);
    return true;
  }

  public Boolean manualRedeemEnableReferralsCampaign(final Market market, final ManualRedeemEnableReferralsCampaignRequest request) {
    productPricingService.manualRedeemEnableReferralsCampaign(market, request);
    return true;
  }


  private String getEmail(DataFetchingEnvironment env) {
    try {
      return GraphQLConfiguration.getEmail(env, personnelService);
    } catch (Exception e) {
      String errorMessage = "Error: Unverified user. Could not get email from personnelService.";
      log.error(errorMessage, e);
      throw new RuntimeException(errorMessage, e);
    }
  }

  private String getToken(DataFetchingEnvironment env) {
    GraphQLRequestContext context = env.getContext();
    return personnelService.getIdToken(context.getUserPrincipal().getName());
  }

  private Personnel getPersonnel(DataFetchingEnvironment env) {
    GraphQLRequestContext context = env.getContext();
    Principal principal = context.getUserPrincipal();
    try {
      return personnelService.getPersonnelByEmail(principal.getName());
    } catch (AuthorizationException exception) {
      log.error("Unauthorized user attempted to get personnel by email", exception);
      throw new RuntimeException("Unable to get personnel");
    }
  }
}

