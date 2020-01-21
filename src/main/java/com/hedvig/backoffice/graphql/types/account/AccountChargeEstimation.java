package com.hedvig.backoffice.graphql.types.account;

import com.hedvig.backoffice.services.account.dto.AccountChargeEstimationDTO;
import lombok.Value;

import javax.money.MonetaryAmount;
import java.util.List;

@Value
public class AccountChargeEstimation {
    MonetaryAmount subscription;
    MonetaryAmount totalDiscountAmount;
    MonetaryAmount charge;
    List<String> discountCodes;

  public static AccountChargeEstimation from(AccountChargeEstimationDTO accountChargeEstimationDTO) {
    return new AccountChargeEstimation(
      accountChargeEstimationDTO.getSubscription(),
        accountChargeEstimationDTO.getTotalDiscountAmount(),
        accountChargeEstimationDTO.getCharge(),
        accountChargeEstimationDTO.getDiscountCodes()
    );
  }
}
