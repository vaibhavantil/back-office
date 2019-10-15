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
public class EarthquakeClaim {
  LocalDate date;

  public static EarthquakeClaim fromClaimData(List<ClaimData> claimData) {
    LocalDate date = null;
    val claimDataWithoutDuplicates = ClaimData.withoutDuplicates(claimData);
    for (val cd : claimDataWithoutDuplicates) {
      switch (cd.getName()) {
        case "DATE": {
          date = LocalDateTime.parse(cd.getValue()).toLocalDate();
          break;
        }
      }
    }
    return new EarthquakeClaim(date);
  }
}
