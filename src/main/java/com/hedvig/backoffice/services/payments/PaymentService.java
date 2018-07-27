package com.hedvig.backoffice.services.payments;

import java.util.List;

import javax.money.MonetaryAmount;

import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;
import com.hedvig.backoffice.services.payments.dto.Transaction;

public interface PaymentService {
    List<Transaction> getTransactionsByMemberId(String memberId);
    Boolean hasDirectDebitActivated(String memberId);
    List<DirectDebitStatusDTO> getDirectDebitStatuses(List<String> memberIds);
	void chargeMember(String memberId, MonetaryAmount amount);
}
