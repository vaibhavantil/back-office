package com.hedvig.backoffice.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import graphql.schema.DataFetchingEnvironment;
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

  public GraphQLMutation(
      PaymentService paymentService, PersonnelService personnelService, MemberLoader memberLoader) {
    this.paymentService = paymentService;
    this.personnelService = personnelService;
    this.memberLoader = memberLoader;
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

  private String getEmail(DataFetchingEnvironment env) throws AuthorizationException {
    GraphQLRequestContext context = env.getContext();
    val personnel = personnelService.getPersonnel(context.getUserPrincipal().getName());
    return personnel.getEmail();
  }
}
