package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.chat.ChatUpdatesService;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceStatusDTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"/api/test"})
public class TestController {
  private final ProductPricingService productPricingService;
  private final ChatUpdatesService chatUpdatesService;

  @Autowired
  public TestController(
    final ProductPricingService productPricingService,
    final ChatUpdatesService chatUpdatesService
  ) {
    this.productPricingService = productPricingService;
    this.chatUpdatesService = chatUpdatesService;
  }

  @GetMapping("/{memberId}")
  public List<InsuranceStatusDTO> getInsurancesByMember(@PathVariable String memberId) {
    return productPricingService.getInsurancesByMember(memberId, null);
  }

  @PostMapping("/refresh-messages")
  public void refreshMessages() {
    chatUpdatesService.update();
  }
}
