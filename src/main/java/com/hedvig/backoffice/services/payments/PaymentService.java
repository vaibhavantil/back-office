package com.hedvig.backoffice.services.payments;

import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;
import com.hedvig.backoffice.services.payments.dto.PayoutMemberRequest;
import com.hedvig.backoffice.services.payments.dto.PayoutRequest;
import com.hedvig.backoffice.services.payments.dto.Transaction;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;
import javax.money.MonetaryAmount;

public interface PaymentService {
  List<Transaction> getTransactionsByMemberId(String memberId);

  List<DirectDebitStatusDTO> getDirectDebitStatuses(List<String> memberIds);

  void chargeMember(String memberId, MonetaryAmount amount);

  Transaction getTransactionById(UUID id);

  DirectDebitStatusDTO getDirectDebitStatusByMemberId(String memberId);

  ResponseEntity<UUID> payoutMember(String memberId, String handler, PayoutMemberRequest payoutMemberRequest);
}
