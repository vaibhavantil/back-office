package com.hedvig.backoffice.services.payments;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;
import com.hedvig.backoffice.services.payments.dto.TransactionDTO;

import org.javamoney.moneta.Money;

public class PaymentServiceStub implements PaymentService {

	@Override
	public List<TransactionDTO> getTransactionsByMemberId(String memberId) {
		return Lists.newArrayList(new TransactionDTO(Money.of(100, "SEK"), Instant.now(), "CHARGE", "COMPLETED"));
	}

	@Override
	public Boolean hasDirectDebitActivated(String memberId) {
		return true;
	}

	@Override
	public List<DirectDebitStatusDTO> getDirectDebitStatuses(List<String> memberIds) {
		return memberIds
			.stream()
			.map(id -> new DirectDebitStatusDTO(id, true))
			.collect(Collectors.toList());
	}
}
