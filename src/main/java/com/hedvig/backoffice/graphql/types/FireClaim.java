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
public class FireClaim {
  String location;
  LocalDate date;
  String item;
  Boolean report;

  public static FireClaim fromClaimData(List<ClaimData> claimData) {
    String location;
    LocalDate date;
    String item;
    Boolean report;

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
    // case "": {

    // }
    // }
    // });
    // return new FireClaim();
  }
}
