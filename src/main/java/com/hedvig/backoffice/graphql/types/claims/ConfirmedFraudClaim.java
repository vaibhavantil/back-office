package com.hedvig.backoffice.graphql.types.claims;

import com.hedvig.backoffice.graphql.UnionType;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import lombok.Value;
import lombok.val;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Value
@UnionType
public class ConfirmedFraudClaim {

  LocalDate date;

  public static ConfirmedFraudClaim fromClaimData(List<ClaimData> d) {
    LocalDate date = null;

    val claimWithoutDuplicates = ClaimData.withoutDuplicates(d);

    for (val cwd : claimWithoutDuplicates) {
      if ("DATE".equals(cwd.getName())) {
        date = LocalDateTime.parse(cwd.getValue()).toLocalDate();
      }
    }
    return new ConfirmedFraudClaim(date);
  }

}
