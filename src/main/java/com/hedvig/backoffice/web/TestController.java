package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = {"/api/test"})
public class TestController {
  private final ProductPricingService productPricingService;

  @Autowired
  public TestController(final ProductPricingService productPricingService) {
    this.productPricingService = productPricingService;
  }

  @GetMapping("/{memberId}")
  public List<InsuranceStatusDTO> getInsurancesByMember(@PathVariable String memberId) {
    return productPricingService.getInsurancesByMember(memberId, null);
  }
}
