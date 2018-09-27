package com.hedvig.backoffice.graphql.types;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import com.hedvig.backoffice.graphql.UnionType;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import lombok.Value;

@Value
@UnionType
public class TheftClaim {
  String location;
  LocalDate date;
  String item;
  String policeReport;
  String receipt;

  public static TheftClaim fromClaimData(List<ClaimData> claimData) {
    String location;
    LocalDate date;
    String item;
    String policeReport;
    String receipt;

    return null;
    // claimData.forEach(cd -> {
    // switch (cd.getName()) {
    // case "PLACE": {
    // location = cd.getValue();
    // }
    // case "DATE": {
    // date = LocalDate.ofInstant(Instant.parse(cd.getValue()), ZoneOffset.UTC);
    // }
    // case "ITEM": {
    // item = cd.getValue();
    // }
    // case "POLICE_REPORT": {
    // policeReport = cd.getValue();
    // }
    // case "RECIEPT": {
    // receipt = cd.getValue();
    // }
    // }
    // });

    // return new TheftClaim(location, date, item, policeReport, receipt);
  }
}
