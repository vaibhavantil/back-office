package com.hedvig.backoffice.services.payments;

import java.util.List;

import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;
import com.hedvig.backoffice.services.payments.dto.TransactionDTO;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentServiceClient paymentServiceClient;

	public PaymentServiceImpl(PaymentServiceClient paymentServiceClient) {
        this.paymentServiceClient = paymentServiceClient;
    }

	@Override
	public List<TransactionDTO> getTransactionsByMemberId(String memberId) {
		return null;
	}

	@Override
	public Boolean hasDirectDebitActivated(String memberId) {
		return null;
	}

	@Override
	public List<DirectDebitStatusDTO> getDirectDebitStatuses(List<String> memberIds) {
        return paymentServiceClient
            .getDirectDebitStatuses(memberIds);
	}

}
