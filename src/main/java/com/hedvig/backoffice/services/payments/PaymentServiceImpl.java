package com.hedvig.backoffice.services.payments;

import com.hedvig.backoffice.config.feign.ExternalServiceBadRequestException;
import com.hedvig.backoffice.config.feign.ExternalServiceNotFoundException;
import com.hedvig.backoffice.services.payments.dto.*;
import feign.FeignException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.money.MonetaryAmount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public class PaymentServiceImpl implements PaymentService {

  private final PaymentServiceClient paymentServiceClient;

  public PaymentServiceImpl(PaymentServiceClient paymentServiceClient) {
    this.paymentServiceClient = paymentServiceClient;
  }

  @Override
  public List<Transaction> getTransactionsByMemberId(String memberId) {
    try {
      return paymentServiceClient.getTransactions(memberId).getTransactions().entrySet().stream()
        .map(entry -> Transaction.fromTransactionDTO(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
    } catch (FeignException e) {
      if (e.status() == 404) {
        return Collections.emptyList();
      }
      log.error("Something went wrong with Payment-service. Status: {}, Message: {}", e.status(),
        e.getMessage());
      throw e;
    } catch (ExternalServiceNotFoundException ex) {
      return Collections.emptyList();
    }
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

  @Override
  public ResponseEntity<UUID> payoutMember(String memberId, PayoutMemberRequest payoutMemberRequest) {
    PayoutRequest payoutRequest = new PayoutRequest(payoutMemberRequest.getAmount(), true); // TODO:  SanctionBypassed from front end
    return paymentServiceClient.payoutMember(
      memberId,
      payoutMemberRequest.getCategory(),
      payoutMemberRequest.getReferenceId(),
      payoutMemberRequest.getNote(),
      payoutRequest
    );
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
