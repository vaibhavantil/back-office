package com.hedvig.backoffice.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.hedvig.backoffice.graphql.dataloaders.ClaimLoader;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Claim;
import com.hedvig.backoffice.graphql.types.ClaimPaymentInput;
import com.hedvig.backoffice.graphql.types.ClaimState;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.dto.ClaimPayment;
import com.hedvig.backoffice.services.claims.dto.ClaimPaymentType;
import com.hedvig.backoffice.services.claims.dto.ClaimStateUpdate;
import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import graphql.schema.DataFetchingEnvironment;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.money.MonetaryAmount;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

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

  public CompletableFuture<Claim> addClaimNote(UUID id, String text, DataFetchingEnvironment env)
      throws AuthorizationException {
    log.info("Personnell with email '{}' adding claim note",
        GraphQLConfiguration.getEmail(env, personnelService));
    val noteDto = new com.hedvig.backoffice.services.claims.dto.ClaimNote();
    noteDto.setText(text);
    noteDto.setClaimID(id.toString());
    claimsService.addNote(noteDto, GraphQLConfiguration.getIdToken(env, personnelService));
    return claimLoader.load(id);
  }

  public CompletableFuture<Claim> createClaimPayment(UUID id, ClaimPaymentInput payment,
      DataFetchingEnvironment env) throws AuthorizationException {
    log.info("Personnel with email '{}'' adding claim payment",
        GraphQLConfiguration.getEmail(env, personnelService));
    val paymentDto = new ClaimPayment();
    paymentDto.setAmount(BigDecimal.valueOf(payment.getAmount().getNumber().doubleValueExact()));
    paymentDto.setNote(payment.getNote());
    paymentDto.setExGratia(payment.getExGratia());
    paymentDto.setPaymentType(ClaimPaymentType.valueOf(payment.getType().toString()));
    paymentDto.setClaimID(id.toString());
    claimsService.addPayment(paymentDto, GraphQLConfiguration.getIdToken(env, personnelService));
    return claimLoader.load(id);
  }

  public CompletableFuture<Claim> setClaimType(UUID id, Object type) {
    return null;
  }

}
