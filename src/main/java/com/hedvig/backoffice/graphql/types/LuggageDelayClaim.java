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
public class LuggageDelayClaim {
  String location;
  LocalDate date;
  String ticket;

  public static LuggageDelayClaim fromClaimData(List<ClaimData> claimData) {
    String location = null;
    LocalDate date = null;
    String ticket = null;

    for (val cd : claimData) {
      switch (cd.getName()) {
        case "DATE": {
          date = LocalDate.ofInstant(Instant.parse(cd.getValue()), ZoneOffset.UTC);
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
