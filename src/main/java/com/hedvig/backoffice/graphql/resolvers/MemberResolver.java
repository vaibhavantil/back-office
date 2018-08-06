package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.dataloaders.DirectDebitStatusLoader;
import com.hedvig.backoffice.graphql.types.DirectDebitStatus;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.MonthlySubscription;
import com.hedvig.backoffice.graphql.types.Transaction;
import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import java.time.YearMonth;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MemberResolver implements GraphQLResolver<Member> {
  private final PaymentService paymentService;
  private final DirectDebitStatusLoader directDebitStatusLoader;
  private final ProductPricingService productPricingService;

  public MemberResolver(
      PaymentService paymentService,
      DirectDebitStatusLoader directDebitStatusLoader,
      ProductPricingService productPricingService) {
    this.paymentService = paymentService;
    this.directDebitStatusLoader = directDebitStatusLoader;
    this.productPricingService = productPricingService;
  }

  public List<Transaction> getTransactions(Member member) {
    return paymentService
        .getTransactionsByMemberId(member.getMemberId())
        .stream()
        .map(
            transactionDTO ->
                new Transaction(
                    transactionDTO.getId(),
                    transactionDTO.getAmount(),
                    transactionDTO.getTimestamp(),
                    transactionDTO.getType(),
                    transactionDTO.getStatus()))
        .collect(Collectors.toList());
  }

  public MonthlySubscription getMonthlySubscription(Member member, YearMonth period) {
    return new MonthlySubscription(
        productPricingService.getMonthlyPaymentsByMember(period, member.getMemberId()));
  }

  public CompletableFuture<DirectDebitStatus> getDirectDebitStatus(Member member) {
    return directDebitStatusLoader.load(member.getMemberId());
  }
}
