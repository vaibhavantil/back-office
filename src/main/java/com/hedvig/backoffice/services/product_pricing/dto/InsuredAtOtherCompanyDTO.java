package com.hedvig.backoffice.services.product_pricing.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class InsuredAtOtherCompanyDTO {

    @NotNull
    private boolean insuredAtOtherCompany;

}
