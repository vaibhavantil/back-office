package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.types.ClaimPayment;
import com.hedvig.backoffice.graphql.types.Transaction;
import com.hedvig.backoffice.services.payments.PaymentService;
import org.springframework.stereotype.Component;

@Component
public class ClaimPaymentResolver implements GraphQLResolver<ClaimPayment> {

  private final PaymentService paymentService;

  public ClaimPaymentResolver(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  public Transaction getTransaction(ClaimPayment claimPayment) {
    if (claimPayment.getTransactionId().isPresent() == false) {
      return null;
    }
    return Transaction
        .fromDTO(paymentService.getTransactionById(claimPayment.getTransactionId().get()));
  }
}
