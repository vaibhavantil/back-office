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
public class TheftClaim {
  String location;
  LocalDate date;
  String item;
  String policeReport;
  String receipt;

  public static TheftClaim fromClaimData(List<ClaimData> claimData) {
    String location = null;
    LocalDate date = null;
    String item = null;
    String policeReport = null;
    String receipt = null;

    for (val cd : claimData) {
      switch (cd.getName()) {
        case "DATE": {
          date = LocalDate.ofInstant(Instant.parse(cd.getValue()), ZoneOffset.UTC);
        }
        case "PLACE": {
          location = cd.getValue();
        }
        case "ITEM": {
          item = cd.getValue();
        }
        case "POLICE_REPORT": {
          policeReport = cd.getValue();
        }
        case "RECEIPT": {
          receipt = cd.getValue();
        }
      }
    }

    return new TheftClaim(location, date, item, policeReport, receipt);
  }
}
