package com.hedvig.backoffice.services.payments;

import java.util.List;

import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentServiceClientFallback implements PaymentServiceClient {
    @Override
    public List<DirectDebitStatusDTO> getDirectDebitStatuses(List<String> memberIds) {
        log.error("payment-service unavailable");
        return null;
    }
}
