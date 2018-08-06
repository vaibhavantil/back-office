package com.hedvig.backoffice.services.payments;

import com.hedvig.backoffice.services.payments.dto.ChargeRequestDTO;
import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;
import com.hedvig.backoffice.services.payments.dto.PaymentMemberDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentServiceClientFallback implements PaymentServiceClient {
  @Override
  public List<DirectDebitStatusDTO> getDirectDebitStatuses(List<String> memberIds) {
    log.error("payment-service unavailable");
    return null;
  }

  @Override
  public PaymentMemberDTO getTransactions(String memberId) {
    log.error("payment-service unavailable");
    return null;
  }

  @Override
  public void chargeMember(String memberId, ChargeRequestDTO chargeRequestDto) {
    log.error("payment-service unavailable");
  }
}
