package com.hedvig.backoffice.graphql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.money.MonetaryAmount;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.hedvig.backoffice.graphql.dataloaders.ClaimLoader;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Claim;
import com.hedvig.backoffice.graphql.types.ClaimInformationInput;
import com.hedvig.backoffice.graphql.types.ClaimNoteInput;
import com.hedvig.backoffice.graphql.types.ClaimPaymentInput;
import com.hedvig.backoffice.graphql.types.ClaimState;
import com.hedvig.backoffice.graphql.types.ClaimTypes;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import com.hedvig.backoffice.services.claims.dto.ClaimPayment;
import com.hedvig.backoffice.services.claims.dto.ClaimPaymentType;
import com.hedvig.backoffice.services.claims.dto.ClaimStateUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimTypeUpdate;
import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import org.springframework.stereotype.Component;
import graphql.schema.DataFetchingEnvironment;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GraphQLMutation implements GraphQLMutationResolver {
  private final MemberLoader memberLoader;
  private final PaymentService paymentService;
  private final PersonnelService personnelService;
  private final ClaimLoader claimLoader;
  private final ClaimsService claimsService;

  public GraphQLMutation(PaymentService paymentService, PersonnelService personnelService,
      MemberLoader memberLoader, ClaimLoader claimLoader, ClaimsService claimsService) {
    this.paymentService = paymentService;
    this.personnelService = personnelService;
    this.memberLoader = memberLoader;
    this.claimLoader = claimLoader;
    this.claimsService = claimsService;
  }

  public CompletableFuture<Member> chargeMember(String id, MonetaryAmount amount,
      DataFetchingEnvironment env) throws AuthorizationException {
    log.info("Personnel with email '{}' attempting to charge member '{}' the amount '{}'",
        GraphQLConfiguration.getEmail(env, personnelService), id, amount.toString());
    paymentService.chargeMember(id, amount);
    return memberLoader.load(id);
  }

  public CompletableFuture<Claim> updateClaimState(UUID id, ClaimState claimState,
      DataFetchingEnvironment env) throws AuthorizationException {
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

  public CompletableFuture<Claim> addClaimNote(UUID id, ClaimNoteInput input,
      DataFetchingEnvironment env) throws AuthorizationException {
    log.info("Personnell with email '{}' adding claim note",
        GraphQLConfiguration.getEmail(env, personnelService));
    val noteDto = new com.hedvig.backoffice.services.claims.dto.ClaimNote();
    noteDto.setText(input.getText());
    noteDto.setClaimID(id.toString());
    claimsService.addNote(noteDto, GraphQLConfiguration.getIdToken(env, personnelService));
    return claimLoader.load(id);
  }

  public CompletableFuture<Claim> createClaimPayment(UUID id, ClaimPaymentInput payment,
      DataFetchingEnvironment env) throws AuthorizationException {
    log.info("Personnel with email '{}'' adding claim payment",
        GraphQLConfiguration.getEmail(env, personnelService));
    val claim =
        claimsService.find(id.toString(), GraphQLConfiguration.getIdToken(env, personnelService));
    val memberId = claim.getUserId();
    val paymentDto = new ClaimPayment();
    paymentDto.setAmount(BigDecimal.valueOf(payment.getAmount().getNumber().doubleValueExact()));
    paymentDto.setNote(payment.getNote());
    paymentDto.setExGratia(payment.getExGratia());
    paymentDto.setPaymentType(ClaimPaymentType.valueOf(payment.getType().toString()));
    paymentDto.setClaimID(id.toString());
    claimsService.addPayment(memberId, paymentDto,
        GraphQLConfiguration.getIdToken(env, personnelService));
    return claimLoader.load(id);
  }

  public CompletableFuture<Claim> setClaimType(UUID id, ClaimTypes type,
      DataFetchingEnvironment env) throws AuthorizationException {
    log.info("Personnel with email '{}' setting claim type",
        GraphQLConfiguration.getEmail(env, personnelService));
    val claimTypeDto = new ClaimTypeUpdate();
    claimTypeDto.setClaimID(id.toString());
    claimTypeDto.setType(Util.claimServiceType(type));
    claimsService.changeType(claimTypeDto, GraphQLConfiguration.getIdToken(env, personnelService));

    return claimLoader.load(id);
  }

  public CompletableFuture<Claim> setClaimInformation(UUID id,
      ClaimInformationInput claimInformationInput, DataFetchingEnvironment env)
      throws AuthorizationException {
    log.info("Personnel with email '{}' updating claim information",
        GraphQLConfiguration.getEmail(env, personnelService));
    val claim =
        claimsService.find(id.toString(), GraphQLConfiguration.getIdToken(env, personnelService));

    val claimData = claim.getData();

    val now = LocalDateTime.now();

    val prevLocation = claimData.stream().filter(d -> d.getName().equals("PLACE"))
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

    val prevDate = claimData.stream().filter(d -> d.getName().equals("DATE"))
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

    val prevItem = claimData.stream().filter(d -> d.getName().equals("ITEM"))
        .sorted(Util.sortedByDateDescComparator).findFirst();
    if (claimInformationInput.getItem() != null && !(prevItem.isPresent()
        && prevItem.get().getValue().equals(claimInformationInput.getItem()))) {
      val data = new ClaimData();
      data.setClaimID(id.toString());
      data.setName("ITEM");
      data.setType("TEXT");
      data.setTitle("Item");
      data.setDate(now);
      data.setValue(claimInformationInput.getItem());
      claimsService.addData(data, GraphQLConfiguration.getIdToken(env, personnelService));
    }

    val prevPoliceReport = claimData.stream().filter(d -> d.getName().equals("POLICE_REPORT"))
        .sorted(Util.sortedByDateDescComparator).findFirst();
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

    val prevReceipt = claimData.stream().filter(d -> d.getName().equals("RECEIPT"))
        .sorted(Util.sortedByDateDescComparator).findFirst();
    if (claimInformationInput.getReceipt() != null && !(prevReceipt.isPresent()
        && prevReceipt.get().getValue().equals(claimInformationInput.getReceipt()))) {
      val data = new ClaimData();
      data.setClaimID(id.toString());
      data.setName("RECEIPT");
      data.setType("RECEIPT");
      data.setTitle("Receipt");
      data.setDate(now);
      data.setValue(claimInformationInput.getReceipt());
      claimsService.addData(data, GraphQLConfiguration.getIdToken(env, personnelService));
    }

    val prevTicket = claimData.stream().filter(d -> d.getName().equals("TICKET"))
        .sorted(Util.sortedByDateDescComparator).findFirst();
    if (claimInformationInput.getTicket() != null && !(prevTicket.isPresent()
        && prevTicket.get().getValue().equals(claimInformationInput.getTicket()))) {
      val data = new ClaimData();
      data.setClaimID(id.toString());
      data.setName("TICKET");
      data.setType("TICKET");
      data.setTitle("Ticket");
      data.setDate(now);
      data.setValue(claimInformationInput.getTicket());
      claimsService.addData(data, GraphQLConfiguration.getIdToken(env, personnelService));
    }

    return claimLoader.load(id);
  }
}
