package com.hedvig.backoffice.graphql.types;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import com.hedvig.backoffice.graphql.UnionType;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import lombok.Value;
import lombok.val;

@Value
@UnionType
public class WaterDamageClaim {
  LocalDate date;

  public static WaterDamageClaim fromClaimData(List<ClaimData> claimData) {
    LocalDate date = null;
    for (val cd : claimData) {
      switch (cd.getName()) {
        case "DATE": {
          date = LocalDate.ofInstant(Instant.parse(cd.getValue()), ZoneOffset.UTC);
        }
      }
    }
    return new WaterDamageClaim(date);
  }
}
