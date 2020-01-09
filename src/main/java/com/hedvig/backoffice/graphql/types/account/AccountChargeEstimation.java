package com.hedvig.backoffice.graphql.types.account;

import com.hedvig.backoffice.services.account.dto.AccountChargeEstimationDTO;
import com.hedvig.backoffice.services.account.dto.AccountEntryDTO;
import com.hedvig.backoffice.services.account.dto.AccountEntryType;
import lombok.Value;

import javax.money.MonetaryAmount;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Value
public class AccountChargeEstimation {
    MonetaryAmount subscription;
    MonetaryAmount discount;
    MonetaryAmount charge;

  public static AccountChargeEstimation from(AccountChargeEstimationDTO accountChargeEstimationDTO) {
    return new AccountChargeEstimation(
      accountChargeEstimationDTO.getSubscription(),
        accountChargeEstimationDTO.getDiscount(),
        accountChargeEstimationDTO.getCharge()
    );
  }
}
