package com.hedvig.backoffice.graphql.resolvers;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.hedvig.backoffice.graphql.GraphQLConfiguration;
import com.hedvig.backoffice.graphql.dataloaders.ClaimLoader;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Claim;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.MonthlySubscription;
import com.hedvig.backoffice.graphql.types.SchedulerStatus;
import com.hedvig.backoffice.services.account.AccountService;
import com.hedvig.backoffice.services.account.ChargeStatus;
import com.hedvig.backoffice.services.account.dto.SchedulerStateDto;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.tickets.TicketsService;

import com.hedvig.backoffice.services.tickets.dto.TicketDto;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import static graphql.servlet.GraphQLServlet.log;

@Component
public class GraphQLQuery implements GraphQLQueryResolver {

  private final ProductPricingService productPricingService;
  private final MemberLoader memberLoader;
  private final ClaimLoader claimLoader;
  private final AccountService accountService;
  private final MemberService memberService;
  private final TicketsService ticketService;
  private final PersonnelService personnelService;

  public GraphQLQuery(ProductPricingService productPricingService, MemberLoader memberLoader,
      ClaimLoader claimLoader, AccountService accountService, MemberService memberService, TicketsService ticketService,
                      PersonnelService personnelService) {
    this.productPricingService = productPricingService;
    this.memberLoader = memberLoader;
    this.claimLoader = claimLoader;
    this.accountService = accountService;
    this.memberService = memberService;
    this.ticketService = ticketService;
    this.personnelService = personnelService;
  }

  public List<MonthlySubscription> monthlyPayments(YearMonth month) {

    return productPricingService.getMonthlyPayments(month).stream()
        .map(ms -> new MonthlySubscription(ms.getMemberId(), ms.getSubscription()))
        .collect(Collectors.toList());
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

  public TicketDto ticket (String id  ){
    return this.ticketService.getTicketById(id);
  }

  public List<TicketDto> tickets (){
    return this.ticketService.getAllTickets();
  }

  public String me (DataFetchingEnvironment env) {
    try {
      return  GraphQLConfiguration.getEmail(env, personnelService);
    }
    catch (Exception e ) {
      log.info("Exception occured when trying to access user email: " + e);
      return null;
    }
  }

}
