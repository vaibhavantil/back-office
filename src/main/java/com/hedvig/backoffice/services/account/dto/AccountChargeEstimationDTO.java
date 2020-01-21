package com.hedvig.backoffice.services.account.dto;

import lombok.Value;

import javax.money.MonetaryAmount;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Value
public class AccountChargeEstimationDTO {
    MonetaryAmount subscription;
    MonetaryAmount discount;
    MonetaryAmount charge;
    List<String> discountCodes;
}
