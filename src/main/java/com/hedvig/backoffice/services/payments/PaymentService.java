package com.hedvig.backoffice.services.payments;

import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;
import com.hedvig.backoffice.services.payments.dto.PayoutMemberRequest;
import com.hedvig.backoffice.services.payments.dto.PayoutMethodStatusDTO;
import com.hedvig.backoffice.services.payments.dto.Transaction;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;
import javax.money.MonetaryAmount;

public interface PaymentService {
    List<Transaction> getTransactionsByMemberId(String memberId);

    List<DirectDebitStatusDTO> getDirectDebitStatuses(List<String> memberIds);

    void chargeMember(String memberId, MonetaryAmount amount, String requestedBy);

    Transaction getTransactionById(UUID id);

    DirectDebitStatusDTO getDirectDebitStatusByMemberId(String memberId);

    PayoutMethodStatusDTO getPayoutMethodStatusByMemberId(@NotNull String memberId);

    ResponseEntity<UUID> payoutMember(String memberId, String handler, PayoutMemberRequest payoutMemberRequest);
}
