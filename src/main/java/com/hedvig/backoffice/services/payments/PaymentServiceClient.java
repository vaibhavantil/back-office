package com.hedvig.backoffice.services.payments;

import java.util.List;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name="payment-service",
    url="${paymentService.baseUrl}",
    configuration=FeignConfig.class,
    fallback=PaymentServiceClientFallback.class
)
public interface PaymentServiceClient {
    @GetMapping("/_/members/directDebitStatus/{memberIds}")
    List<DirectDebitStatusDTO> getDirectDebitStatuses(@PathVariable("memberIds") List<String> memberIds);
}
