package com.hedvig.backoffice.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.dto.ClaimSource;
import com.hedvig.backoffice.services.claims.dto.CreateBackofficeClaimDTO;
import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import graphql.schema.DataFetchingEnvironment;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.money.MonetaryAmount;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import static com.hedvig.backoffice.util.TzHelper.SWEDEN_TZ;

@Component
@Slf4j
public class GraphQLMutation implements GraphQLMutationResolver {
  private final MemberLoader memberLoader;
  private final PaymentService paymentService;
  private final PersonnelService personnelService;
  private final ClaimsService claimsService;

  public GraphQLMutation(
    PaymentService paymentService, PersonnelService personnelService, MemberLoader memberLoader, ClaimsService claimsService) {
    this.paymentService = paymentService;
    this.personnelService = personnelService;
    this.memberLoader = memberLoader;
    this.claimsService = claimsService;
  }

  public CompletableFuture<Member> chargeMember(
      String id, MonetaryAmount amount, DataFetchingEnvironment env) throws AuthorizationException {
    log.info(
        "Personnel with email '{}' attempting to charge member '{}' the amount '{}'",
        getEmail(env),
        id,
        amount.toString());
    paymentService.chargeMember(id, amount);
    return memberLoader.load(id);
  }

  public UUID createClaim(String memberId, LocalDateTime date, ClaimSource source, DataFetchingEnvironment env) {
    GraphQLRequestContext context = env.getContext();
    String token = personnelService.getIdToken(context.getUserPrincipal().getName());
    return claimsService.createClaim(new CreateBackofficeClaimDTO(
      memberId,
      date.atZone(SWEDEN_TZ).toInstant(),
      source
    ), token);
  }



  private String getEmail(DataFetchingEnvironment env) throws AuthorizationException {
    GraphQLRequestContext context = env.getContext();
    val personnel = personnelService.getPersonnel(context.getUserPrincipal().getName());
    return personnel.getEmail();
  }
}
