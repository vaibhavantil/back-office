package com.hedvig.backoffice.graphql.types.claims;

import com.hedvig.backoffice.graphql.UnionType;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Value;
import lombok.val;

@Value
@UnionType
public class NotCoveredClaim {

  LocalDate date;

  public static NotCoveredClaim fromClaimData(List<ClaimData> d) {
    LocalDate date = null;

    val claimWithoutDuplicates = ClaimData.withoutDuplicates(d);

    for (val cwd : claimWithoutDuplicates) {
      if ("DATE".equals(cwd.getName())) {
        date = LocalDateTime.parse(cwd.getValue()).toLocalDate();
      }
    }
    return new NotCoveredClaim(date);
  }

}
