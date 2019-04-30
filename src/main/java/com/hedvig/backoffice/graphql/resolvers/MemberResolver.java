package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.types.*;
import com.hedvig.backoffice.services.meerkat.Meerkat;
import com.hedvig.backoffice.services.meerkat.dto.SanctionStatus;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;

import java.io.File;
import java.time.Instant;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MemberResolver implements GraphQLResolver<Member> {

  private final PaymentService paymentService;
  private final ProductPricingService productPricingService;
  private final Meerkat meerkat;
  private final BotService botService;

  public MemberResolver(
    PaymentService paymentService,
    ProductPricingService productPricingService,
    Meerkat meerkat,
    BotService botService
  ) {
    this.paymentService = paymentService;
    this.productPricingService = productPricingService;
    this.meerkat = meerkat;
    this.botService = botService;
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
  public List<FileUpload> fileUploads(Member member) {
    return botService.files(member.getMemberId(), "123a").stream()
      .map(fileUploadDTO -> FileUpload.from(fileUploadDTO))
      .collect(Collectors.toList());
  }

}
