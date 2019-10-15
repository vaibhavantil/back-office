package com.hedvig.backoffice.graphql.types.claims;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    val claimDataWithoutDuplicates = ClaimData.withoutDuplicates(claimData);

    for (val cd : claimDataWithoutDuplicates) {
      switch (cd.getName()) {
        case "PLACE": {
          location = cd.getValue();
          break;
        }
        case "DATE": {
          date = LocalDateTime.parse(cd.getValue()).toLocalDate();
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
