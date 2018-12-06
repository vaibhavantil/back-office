package com.hedvig.backoffice.services.payments;

import com.hedvig.backoffice.config.feign.ExternalServiceBadRequestException;
import com.hedvig.backoffice.services.payments.dto.ChargeRequestDTO;
import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;
import com.hedvig.backoffice.services.payments.dto.Transaction;
import com.hedvig.backoffice.services.payments.dto.TransactionDTO;
import feign.FeignException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.money.MonetaryAmount;
import org.springframework.http.ResponseEntity;

public class PaymentServiceImpl implements PaymentService {

  private final PaymentServiceClient paymentServiceClient;

  public PaymentServiceImpl(PaymentServiceClient paymentServiceClient) {
    this.paymentServiceClient = paymentServiceClient;
  }

  @Override
  public List<Transaction> getTransactionsByMemberId(String memberId) {
    return paymentServiceClient.getTransactions(memberId).getTransactions().entrySet().stream()
      .map(entry -> Transaction.fromTransactionDTO(entry.getKey(), entry.getValue()))
      .collect(Collectors.toList());
  }

  @Override
  public List<DirectDebitStatusDTO> getDirectDebitStatuses(List<String> memberIds) {
    return paymentServiceClient.getDirectDebitStatuses(memberIds);
  }

  @Override
  public void chargeMember(String memberId, MonetaryAmount amount) {
    paymentServiceClient.chargeMember(memberId, new ChargeRequestDTO(amount));
  }

  @Override
  public Transaction getTransactionById(UUID id) {
    TransactionDTO dto = paymentServiceClient.getTransactionById(id).getBody();
    return Transaction.fromTransactionDTO(id, dto);
  }

  public DirectDebitStatusDTO getDirectDebitStatusByMemberId(String memberId) {
    try {
      ResponseEntity<?> status = paymentServiceClient.getDirectDebitStatusByMemberId(memberId);
      return (DirectDebitStatusDTO) status.getBody();
    } catch (FeignException | ExternalServiceBadRequestException ex) {
      return new DirectDebitStatusDTO(memberId, false);
    }
  }
}
