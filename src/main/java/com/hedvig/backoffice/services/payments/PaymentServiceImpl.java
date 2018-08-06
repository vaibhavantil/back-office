package com.hedvig.backoffice.services.payments;

import com.hedvig.backoffice.services.payments.dto.ChargeRequestDTO;
import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;
import com.hedvig.backoffice.services.payments.dto.Transaction;
import java.util.List;
import java.util.stream.Collectors;
import javax.money.MonetaryAmount;

public class PaymentServiceImpl implements PaymentService {

  private final PaymentServiceClient paymentServiceClient;

  public PaymentServiceImpl(PaymentServiceClient paymentServiceClient) {
    this.paymentServiceClient = paymentServiceClient;
  }

  @Override
  public List<Transaction> getTransactionsByMemberId(String memberId) {
    return paymentServiceClient
        .getTransactions(memberId)
        .getTransactions()
        .entrySet()
        .stream()
        .map(entry -> Transaction.fromTransactionDTO(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  @Override
  public Boolean hasDirectDebitActivated(String memberId) {
    return null;
  }

  @Override
  public List<DirectDebitStatusDTO> getDirectDebitStatuses(List<String> memberIds) {
    return paymentServiceClient.getDirectDebitStatuses(memberIds);
  }

  @Override
  public void chargeMember(String memberId, MonetaryAmount amount) {
    paymentServiceClient.chargeMember(memberId, new ChargeRequestDTO(amount));
  }
}
