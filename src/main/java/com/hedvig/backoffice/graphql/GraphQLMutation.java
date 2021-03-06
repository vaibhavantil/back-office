package com.hedvig.backoffice.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.graphql.dataloaders.ClaimLoader;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.AssignVoucherFreeMonths;
import com.hedvig.backoffice.graphql.types.AssignVoucherPercentageDiscount;
import com.hedvig.backoffice.graphql.types.AssignVoucherVisibleNoDiscount;
import com.hedvig.backoffice.graphql.types.Claim;
import com.hedvig.backoffice.graphql.types.ClaimInformationInput;
import com.hedvig.backoffice.graphql.types.ClaimNoteInput;
import com.hedvig.backoffice.graphql.types.ClaimPaymentInput;
import com.hedvig.backoffice.graphql.types.ClaimState;
import com.hedvig.backoffice.graphql.types.ClaimSwishPaymentInput;
import com.hedvig.backoffice.graphql.types.ClaimTypes;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.MemberChargeApproval;
import com.hedvig.backoffice.graphql.types.PaymentCompletionResponse;
import com.hedvig.backoffice.graphql.types.Quote;
import com.hedvig.backoffice.graphql.types.SendMessageInput;
import com.hedvig.backoffice.graphql.types.SendMessageResponse;
import com.hedvig.backoffice.graphql.types.account.AccountEntryInput;
import com.hedvig.backoffice.graphql.types.account.MonthlyEntryInput;
import com.hedvig.backoffice.graphql.types.claims.SetContractForClaim;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.account.AccountService;
import com.hedvig.backoffice.services.account.dto.ApproveChargeRequestDto;
import com.hedvig.backoffice.services.apigateway.ApiGatewayService;
import com.hedvig.backoffice.services.chat.ChatServiceV2;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.dto.ClaimSource;
import com.hedvig.backoffice.services.claims.dto.ClaimPayment;
import com.hedvig.backoffice.services.claims.dto.ClaimPaymentType;
import com.hedvig.backoffice.services.claims.dto.CreateBackofficeClaimDTO;
import com.hedvig.backoffice.services.claims.dto.ClaimReserveUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimTypeUpdate;
import com.hedvig.backoffice.services.claims.dto.EmployeeClaimRequestDTO;
import com.hedvig.backoffice.services.claims.dto.ClaimStateUpdate;
import com.hedvig.backoffice.services.claims.dto.MarkClaimFileAsDeletedDTO;
import com.hedvig.backoffice.services.claims.dto.ClaimFileCategoryDTO;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import com.hedvig.backoffice.services.itemizer.ItemizerService;
import com.hedvig.backoffice.services.itemizer.dto.request.InsertItemCategoriesRequest;
import com.hedvig.backoffice.services.itemizer.dto.request.InsertValuationRulesRequest;
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertClaimItemRequest;
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertItemBrandRequest;
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertItemCompanyRequest;
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertItemModelRequest;
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertItemTypeRequest;
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertValuationRuleRequest;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.dto.EditMemberInfoRequest;
import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.services.payments.dto.SelectedPayoutDetails;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.priceEngine.PriceEngineService;
import com.hedvig.backoffice.services.priceEngine.dto.CreateNorwegianGripenRequest;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.dto.AssignVoucherFreeMonthsRequest;
import com.hedvig.backoffice.services.product_pricing.dto.AssignVoucherPercentageDiscountRequest;
import com.hedvig.backoffice.services.product_pricing.dto.AssignVoucherVisibleNoDiscountRequest;
import com.hedvig.backoffice.services.product_pricing.dto.ManualRedeemCampaignRequest;
import com.hedvig.backoffice.services.product_pricing.dto.ManualUnRedeemCampaignRequest;
import com.hedvig.backoffice.services.product_pricing.dto.contract.ActivatePendingAgreementRequest;
import com.hedvig.backoffice.services.product_pricing.dto.contract.ChangeFromDateOnAgreementRequest;
import com.hedvig.backoffice.services.product_pricing.dto.contract.ChangeTerminationDateRequest;
import com.hedvig.backoffice.services.product_pricing.dto.contract.ChangeToDateOnAgreementRequest;
import com.hedvig.backoffice.services.product_pricing.dto.contract.Contract;
import com.hedvig.backoffice.services.product_pricing.dto.contract.SafelyEditAgreementRequest;
import com.hedvig.backoffice.services.product_pricing.dto.contract.TerminateContractRequest;
import com.hedvig.backoffice.services.qualityassurance.QualityAssuranceService;
import com.hedvig.backoffice.services.qualityassurance.dto.UnsignMemberRequest;
import com.hedvig.backoffice.services.questions.QuestionNotFoundException;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.underwriter.UnderwriterService;
import com.hedvig.backoffice.services.underwriter.dtos.QuoteFromAgreementRequestDto;
import com.hedvig.backoffice.services.underwriter.dtos.QuoteResponseDto;
import com.hedvig.backoffice.services.underwriter.dtos.SignQuoteFromHopeRequestDto;
import com.hedvig.backoffice.web.dto.MemberFraudulentStatusDTO;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.execution.DataFetcherResult;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;

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
import javax.money.MonetaryAmount;

import jersey.repackaged.com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
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
    private final QuestionService questionsService;
    private final MemberService memberService;
    private final UnderwriterService underwriterService;
    private final ProductPricingService productPricingService;
    private final PriceEngineService priceEngineService;
    private final ChatServiceV2 chatServiceV2;
    private final ItemizerService itemizerService;
    private final ApiGatewayService apiGatewayService;
    private final QualityAssuranceService qualityAssuranceService;

    public GraphQLMutation(
        PaymentService paymentService,
        PersonnelService personnelService,
        MemberLoader memberLoader,
        ClaimLoader claimLoader,
        ClaimsService claimsService,
        AccountService accountService,
        QuestionService questionsService,
        MemberService memberService,
        UnderwriterService underwriterService,
        ProductPricingService productPricingService,
        PriceEngineService priceEngineService,
        ChatServiceV2 chatServiceV2,
        ItemizerService itemizerService,
        final ApiGatewayService apiGatewayService,
        QualityAssuranceService qualityAssuranceService
    ) {
        this.paymentService = paymentService;
        this.personnelService = personnelService;
        this.memberLoader = memberLoader;
        this.claimLoader = claimLoader;
        this.claimsService = claimsService;
        this.accountService = accountService;
        this.questionsService = questionsService;
        this.memberService = memberService;
        this.underwriterService = underwriterService;
        this.productPricingService = productPricingService;
        this.priceEngineService = priceEngineService;
        this.chatServiceV2 = chatServiceV2;
        this.itemizerService = itemizerService;
        this.apiGatewayService = apiGatewayService;
        this.qualityAssuranceService = qualityAssuranceService;
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

    public CompletableFuture<Member> addMonthlyEntryToMember(
        final String memberId,
        final MonthlyEntryInput monthlyEntryInput,
        final DataFetchingEnvironment env
    ) throws AuthorizationException {
        accountService.addMonthlyEntry(memberId, monthlyEntryInput, GraphQLConfiguration.getEmail(env, personnelService));
        return memberLoader.load(memberId);
    }

    public Boolean removeMonthlyEntry(
        final UUID id,
        final DataFetchingEnvironment env
    ) throws AuthorizationException {
        String email = GraphQLConfiguration.getEmail(env, personnelService);
        accountService.removeMonthlyEntry(id, email);
        return true;
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
                .map(ApproveChargeRequestDto.Companion::from)
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
        final String employeeEmail = GraphQLConfiguration.getEmail(env, personnelService);
        log.info("Personnell with email '{}' adding claim note", employeeEmail);
        val noteDto = new com.hedvig.backoffice.services.claims.dto.ClaimNote();
        noteDto.setText(input.getText());
        noteDto.setClaimID(id.toString());
        noteDto.setHandlerReference(employeeEmail);
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
        ClaimPaymentInput paymentInput,
        DataFetchingEnvironment env
    ) throws AuthorizationException {
        val claim =
            claimsService.find(id.toString(), GraphQLConfiguration.getIdToken(env, personnelService));
        val claimPayment = new ClaimPayment(
            id.toString(),
            paymentInput.getAmount(),
            paymentInput.getDeductible(),
            paymentInput.getNote(),
            paymentInput.getExGratia(),
            ClaimPaymentType.valueOf(paymentInput.getType().toString()),
            GraphQLConfiguration.getEmail(env, personnelService),
            paymentInput.isSanctionListSkipped(),
            null,
            null,
            null,
            null,
            paymentInput.getCarrier(),
            SelectedPayoutDetails.NotSelected.INSTANCE
        );
        return addPayment(id, claimPayment, env);
    }

    public CompletableFuture<DataFetcherResult<Claim>> createClaimSwishPayment(
        UUID id,
        ClaimSwishPaymentInput paymentInput,
        DataFetchingEnvironment env
    ) throws AuthorizationException {
        log.info("Personnel with email '{}'' adding claim payment",
            GraphQLConfiguration.getEmail(env, personnelService));
        val claimPayment = new ClaimPayment(
            id.toString(),
            paymentInput.getAmount(),
            paymentInput.getDeductible(),
            paymentInput.getNote(),
            paymentInput.getExGratia(),
            ClaimPaymentType.Automatic,
            GraphQLConfiguration.getEmail(env, personnelService),
            paymentInput.getSanctionListSkipped(),
            null,
            null,
            null,
            null,
            paymentInput.getCarrier(),
            new SelectedPayoutDetails.Swish(
                paymentInput.getPhoneNumber(),
                paymentInput.getMessage()
            )
        );
        return addPayment(id, claimPayment, env);
    }

    private CompletableFuture<DataFetcherResult<Claim>> addPayment(
        UUID id,
        ClaimPayment claimPayment,
        DataFetchingEnvironment env
    ) {
        switch (claimsService.addPayment(claimPayment,
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
        @Nullable final LocalDate previousAgreementActiveTo,
        final DataFetchingEnvironment env
    ) {
        return Quote.from(underwriterService.addAgreementFromQuote(id, contractId, activeFrom, activeTo, previousAgreementActiveTo, getToken(env)));
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

    Quote updateQuoteBySchema(
        final UUID quoteId,
        final JsonNode schemaData,
        final boolean bypassUnderwritingGuidelines,
        final DataFetchingEnvironment env
    ) {
        final String underwritingGuidelinesBypassedBy = bypassUnderwritingGuidelines ? getEmail(env) : null;
        final QuoteResponseDto response = underwriterService.updateQuoteBySchemaData(quoteId, schemaData, underwritingGuidelinesBypassedBy);
        return Quote.from(underwriterService.getQuote(response.getId()));
    }

    Quote createQuoteForMemberBySchema(
        final String memberId,
        final JsonNode schemaData,
        final boolean bypassUnderwritingGuidelines,
        final DataFetchingEnvironment env
    ) {
        final String underwritingGuidelinesBypassedBy = bypassUnderwritingGuidelines ? getEmail(env) : null;
        final QuoteResponseDto response = underwriterService.createQuoteForMemberBySchemaData(memberId, schemaData, underwritingGuidelinesBypassedBy);
        return Quote.from(underwriterService.getQuote(response.getId()));
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

    public UUID safelyEdit(final UUID agreementId, final SafelyEditAgreementRequest request, DataFetchingEnvironment env) {
        productPricingService.safelyEdit(
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

    public Boolean assignCampaignToPartnerFreeMonths(AssignVoucherFreeMonths request, DataFetchingEnvironment env) {
        productPricingService.assignCampaignToPartnerFreeMonths(
            AssignVoucherFreeMonthsRequest.Companion.from(request)
        );
        return true;
    }

    public Boolean assignCampaignToPartnerVisibleNoDiscount(AssignVoucherVisibleNoDiscount request, DataFetchingEnvironment env) {
        productPricingService.assignCampaignToPartnerVisibleNoDiscount(
            AssignVoucherVisibleNoDiscountRequest.Companion.from(request)
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

    public Boolean unsignMember(final String ssn) {
        qualityAssuranceService.unsignMember(new UnsignMemberRequest(ssn));
        return true;
    }

    public Boolean editMemberInfo(final EditMemberInfoRequest request, DataFetchingEnvironment env) {
        memberService.editMemberInfo(request, getToken(env));
        return true;
    }

    public Boolean setFraudulentStatus(final String memberId, final MemberFraudulentStatusDTO request, DataFetchingEnvironment env) {
        memberService.setFraudulentStatus(memberId, request, getToken(env));
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

