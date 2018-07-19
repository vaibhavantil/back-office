package com.hedvig.backoffice.services.payments;

import java.util.List;

import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;
import com.hedvig.backoffice.services.payments.dto.TransactionDTO;

public interface PaymentService {
    List<TransactionDTO> getTransactionsByMemberId(String memberId);
    Boolean hasDirectDebitActivated(String memberId);
    List<DirectDebitStatusDTO> getDirectDebitStatuses(List<String> memberIds);
}
