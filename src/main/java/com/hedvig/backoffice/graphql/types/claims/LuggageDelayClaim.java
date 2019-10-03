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
public class LuggageDelayClaim {
  String location;
  LocalDate date;
  String ticket;

  public static LuggageDelayClaim fromClaimData(List<ClaimData> claimData) {
    String location = null;
    LocalDate date = null;
    String ticket = null;

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
        case "TICKET": {
          ticket = cd.getValue();
          break;
        }
      }
    }

    return new LuggageDelayClaim(location, date, ticket);
  }
}
