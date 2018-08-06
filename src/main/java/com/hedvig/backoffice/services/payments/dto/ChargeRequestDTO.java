package com.hedvig.backoffice.services.payments.dto;

import javax.money.MonetaryAmount;
import lombok.Value;

@Value
public class ChargeRequestDTO {
  MonetaryAmount amount;
}
