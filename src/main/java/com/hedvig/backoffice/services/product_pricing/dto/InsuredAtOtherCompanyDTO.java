package com.hedvig.backoffice.services.product_pricing.dto;

import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class InsuredAtOtherCompanyDTO {

  @NotNull private boolean insuredAtOtherCompany;
}
