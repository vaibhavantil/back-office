package com.hedvig.backoffice.graphql.types;

import com.hedvig.backoffice.graphql.UnionType;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import lombok.Value;
import lombok.val;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Value
@UnionType
public class ApplianceClaim {
  String location;
  LocalDate date;
  String item;

  public static ApplianceClaim fromClaimData(List<ClaimData> claimData) {
    String location = null;
    LocalDate date = null;
    String item = null;

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
        case "ITEM": {
          item = cd.getValue();
          break;
        }
      }
    }

    return new ApplianceClaim(location, date, item);
  }
}
