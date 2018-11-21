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
public class AssaultClaim {
  String location;
  LocalDate date;
  String policeReport;

  public static AssaultClaim fromClaimData(List<ClaimData> claimData) {
    String location = null;
    LocalDate date = null;
    String policeReport = null;

    for (val cd : claimData) {
      switch (cd.getName()) {
        case "PLACE": {
          location = cd.getValue();
          break;
        }
        case "DATE": {
          date = LocalDate.ofInstant(Instant.parse(cd.getValue()), ZoneOffset.UTC);
          break;
        }
        case "POLICE_REPORT": {
          policeReport = cd.getValue();
          break;
        }
      }
    }
    return new AssaultClaim(location, date, policeReport);
  }
}
