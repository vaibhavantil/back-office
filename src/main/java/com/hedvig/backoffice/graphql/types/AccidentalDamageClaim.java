package com.hedvig.backoffice.graphql.types;

import java.time.LocalDate;
import java.util.List;
import com.hedvig.backoffice.graphql.UnionType;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import lombok.Value;

@Value
@UnionType
public class AccidentalDamageClaim {
  String location;
  LocalDate date;
  String item;
  String policeReport;
  String receipt;

  public static AccidentalDamageClaim fromClaimData(List<ClaimData> claimData) {
    return null;
  }
}
