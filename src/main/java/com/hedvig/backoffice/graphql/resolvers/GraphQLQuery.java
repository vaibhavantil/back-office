package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.fasterxml.jackson.databind.JsonNode;
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
import com.hedvig.backoffice.services.chat.ChatServiceV2;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.dto.ClaimSearchResultDTO;
import com.hedvig.backoffice.services.claims.dto.ClaimSortColumn;
import com.hedvig.backoffice.services.itemizer.ItemizerService;
import com.hedvig.backoffice.services.itemizer.dto.CanValuateClaimItem;
import com.hedvig.backoffice.services.itemizer.dto.ClaimItem;
import com.hedvig.backoffice.services.itemizer.dto.ClaimItemValuation;
import com.hedvig.backoffice.services.itemizer.dto.TotalClaimItemValuation;
import com.hedvig.backoffice.services.itemizer.dto.request.GetClaimItemValuationRequest;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.dto.MembersSearchResultDTO;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.dto.PartnerCampaignSearchResponse;
import com.hedvig.backoffice.services.product_pricing.dto.PartnerResponseDto;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.underwriter.UnderwriterService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

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
    private final PersonnelService personnelService;
    private final ChatServiceV2 chatServiceV2;
    private final QuestionService questionService;
    private final QuestionGroupRepository questionGroupRepository;
    private final ClaimsService claimsService;
    private final MemberService memberService;
    private final UnderwriterService underwriterService;

    public GraphQLQuery(
        ProductPricingService productPricingService,
        MemberLoader memberLoader,
        ClaimLoader claimLoader,
        AccountService accountService,
        MemberService memberService,
        PersonnelService personnelService,
        ChatServiceV2 chatServiceV2,
        QuestionService questionService,
        ItemizerService itemizerService,
        QuestionGroupRepository questionGroupRepository,
        ClaimsService claimsService,
        UnderwriterService underwriterService
    ) {
        this.productPricingService = productPricingService;
        this.memberLoader = memberLoader;
        this.claimLoader = claimLoader;
        this.accountService = accountService;
        this.itemizerService = itemizerService;
        this.personnelService = personnelService;
        this.chatServiceV2 = chatServiceV2;
        this.questionService = questionService;
        this.questionGroupRepository = questionGroupRepository;
        this.claimsService = claimsService;
        this.memberService = memberService;
        this.underwriterService = underwriterService;
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
        long claims = claimsService.totalClaims(token);
        long questions = questionGroupRepository.notAnsweredCount();
        return new DashboardNumbers(claims, questions);
    }

    private String getToken(DataFetchingEnvironment env) {
        GraphQLRequestContext context = env.getContext();
        return personnelService.getIdToken(context.getUserPrincipal().getName());
    }

    public ClaimItemValuation getClaimItemValuation(GetClaimItemValuationRequest request) {
        return itemizerService.getClaimItemValuation(request);
    }

    public TotalClaimItemValuation getTotalClaimItemValuation(UUID claimId, String typeOfContract) {
        return itemizerService.getTotalClaimItemValuation(claimId, typeOfContract);
    }

    public CanValuateClaimItem canValuateClaimItem(String typeOfContract, String itemFamilyId, UUID itemTypeId) {
        return itemizerService.canValuateClaimItem(typeOfContract, itemFamilyId, itemTypeId);
    }

    public String describeClaimItemValuation(UUID claimItemId, String typeOfContract) {
        return itemizerService.describeClaimItemValuation(claimItemId, typeOfContract);
    }

    public MemberSearchResult memberSearch(String query, MemberSearchOptions options, DataFetchingEnvironment env) {
        MembersSearchResultDTO searchResult = memberService.searchPaged(
            options.getIncludeAll(),
            query,
            options.getPage(),
            options.getPageSize(),
            options.getSortBy(),
            options.getSortDirection(),
            getToken(env)
        );

        return MemberSearchResult.Companion.from(searchResult);
    }

    public ListClaimsResult listClaims(ListClaimsOptions options, DataFetchingEnvironment env) {
        ClaimSearchResultDTO result =
            claimsService.search(
                options.getPage(),
                options.getPageSize(),
                options.getSortBy(),
                options.getSortDirection(),
                getToken(env)
            );

        return ListClaimsResult.Companion.from(
            result
        );
    }

    public JsonNode getQuoteSchemaForContractType(String contractType) {
        return underwriterService.getSchemaForContractType(contractType);
    }

    private String getEmail(DataFetchingEnvironment env) {
        try {
            return GraphQLConfiguration.getEmail(env, personnelService);
        } catch (AuthorizationException e) {
            throw new RuntimeException("Failed to get email from GraphQLConfiguration", e);
        }
    }
}
