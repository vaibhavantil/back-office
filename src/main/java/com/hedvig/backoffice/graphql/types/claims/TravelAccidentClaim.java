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
public class TravelAccidentClaim {
  String location;
  LocalDate date;
  String policeReport;
  String receipt;

  public static TravelAccidentClaim fromClaimData(List<ClaimData> claimData) {
    String location = null;
    LocalDate date = null;
    String policeReport = null;
    String receipt = null;

    val claimDataWithoutDuplicates = ClaimData.withoutDuplicates(claimData);

    for (val cd : claimDataWithoutDuplicates) {
      switch (cd.getName()) {
        case "DATE": {
          date = LocalDateTime.parse(cd.getValue()).toLocalDate();
          break;
        }
        case "PLACE": {
          location = cd.getValue();
          break;
        }
        case "POLICE_REPORT": {
          policeReport = cd.getValue();
          break;
        }
        case "RECEIPT": {
          receipt = cd.getValue();
          break;
        }
      }
    }

    return new TravelAccidentClaim(location, date, policeReport, receipt);
  }
}
