package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.dataloaders.AccountLoader;
import com.hedvig.backoffice.graphql.types.*;
import com.hedvig.backoffice.services.meerkat.Meerkat;
import com.hedvig.backoffice.services.meerkat.dto.SanctionStatus;
import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class MemberResolver implements GraphQLResolver<Member> {

  private final PaymentService paymentService;
  private final ProductPricingService productPricingService;
  private final Meerkat meerkat;
  private final AccountLoader accountLoader;

  public MemberResolver(
    PaymentService paymentService,
    ProductPricingService productPricingService,
    Meerkat meerkat,
    AccountLoader accountLoader
  ) {
    this.paymentService = paymentService;
    this.productPricingService = productPricingService;
    this.meerkat = meerkat;
    this.accountLoader = accountLoader;
  }

  public List<Transaction> getTransactions(Member member) {
    return paymentService.getTransactionsByMemberId(member.getMemberId()).stream()
      .map(dto -> Transaction.fromDTO(dto)).collect(Collectors.toList());
  }

  public MonthlySubscription getMonthlySubscription(Member member, YearMonth period) {
    return new MonthlySubscription(
      productPricingService.getMonthlyPaymentsByMember(period, member.getMemberId()));
  }

  public DirectDebitStatus getDirectDebitStatus(Member member) {
    DirectDebitStatusDTO statusDTO =
      paymentService.getDirectDebitStatusByMemberId(member.getMemberId());
    if (statusDTO == null) {
      return new DirectDebitStatus(member.getMemberId(), false);
    }
    return new DirectDebitStatus(statusDTO.getMemberId(), statusDTO.isDirectDebitActivated());
  }

  public SanctionStatus getSanctionStatus(Member member) {
    return meerkat.getMemberSanctionStatus(String.format("%s %s", member.getFirstName(), member.getLastName()));
  }

  public CompletableFuture<Account> getAccount(Member member) {
    return accountLoader.load(member.getMemberId());
  }
}
