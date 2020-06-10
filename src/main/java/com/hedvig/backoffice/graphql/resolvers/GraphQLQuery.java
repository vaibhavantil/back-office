package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.hedvig.backoffice.graphql.GraphQLConfiguration;
import com.hedvig.backoffice.graphql.dataloaders.ClaimLoader;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.*;
import com.hedvig.backoffice.graphql.types.account.SchedulerStatus;
import com.hedvig.backoffice.services.account.AccountService;
import com.hedvig.backoffice.services.account.ChargeStatus;
import com.hedvig.backoffice.services.account.dto.SchedulerStateDto;
import com.hedvig.backoffice.services.autoAnswerSuggestion.AutoAnswerSuggestionService;
import com.hedvig.backoffice.services.autoAnswerSuggestion.DTOs.SuggestionDTO;
import com.hedvig.backoffice.services.itemPricing.ItemPricingService;
import com.hedvig.backoffice.services.itemPricing.dto.CategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ClaimInventoryItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterSuggestionDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemPricepointDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchQueryDTO;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.product_pricing.PartnerResponseDto;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.tickets.TicketService;
import com.hedvig.backoffice.services.tickets.dto.TicketDto;
import com.hedvig.backoffice.services.tickets.dto.TicketHistoryDto;
import graphql.schema.DataFetchingEnvironment;
import lombok.val;
import org.springframework.stereotype.Component;

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
  private final MemberService memberService;
  private final ItemPricingService itemPricingService;
  private final TicketService ticketService;
  private final PersonnelService personnelService;
  private final AutoAnswerSuggestionService autoAnswerSuggestionService;

  public GraphQLQuery(
    ProductPricingService productPricingService,
    MemberLoader memberLoader,
    ClaimLoader claimLoader,
    AccountService accountService,
    MemberService memberService,
    TicketService ticketService,
    PersonnelService personnelService,
    AutoAnswerSuggestionService autoAnswerSuggestionService,
    ItemPricingService itemPricingService
  ) {
    this.productPricingService = productPricingService;
    this.memberLoader = memberLoader;
    this.claimLoader = claimLoader;
    this.accountService = accountService;
    this.memberService = memberService;
    this.itemPricingService = itemPricingService;
    this.ticketService = ticketService;
    this.personnelService = personnelService;
    this.autoAnswerSuggestionService = autoAnswerSuggestionService;
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

  public List<CategoryDTO> categories() {
    return itemPricingService.getCategories();
  }

  public ItemSearchDTO items(ItemSearchQueryDTO payload) {
    return itemPricingService.getItems(payload);
  }

  public List<ItemPricepointDTO> prices(String date, List<String> ids) {
    return itemPricingService.getPrices(date, ids);
  }

  public List<ClaimInventoryItemDTO> inventory(String claimId) {
    return itemPricingService.getInventory(claimId);
  }

  public List<FilterSuggestionDTO> filters(String categoryId) {

    return itemPricingService.getAllFilters(categoryId);
  }

  public List<FilterDTO> inventoryItemFilters(String inventoryItemId) {
    return itemPricingService.getInventoryItemFilters(inventoryItemId);
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

  public List<VoucherCampaign> findPartnerCampaigns(CampaignFilter filter) {
    final var code = filter == null ? null : filter.getCode();
    final var partnerId = filter == null ? null : filter.getPartnerId();
    final var activeFrom = filter == null ? null : filter.getActiveFrom();
    final var activeTo = filter == null ? null : filter.getActiveTo();
    return productPricingService.searchPartnerCampaigns(code, partnerId, activeFrom, activeTo)
      .stream().map(partnerCampaignSearchResponse ->
      VoucherCampaign.Companion.from(partnerCampaignSearchResponse)
    ).collect(Collectors.toList());
  }

  public List<PartnerResponseDto> getPartnerCampaignOwners() {
    return productPricingService.getPartnerCampaignOwners();
  }
}

