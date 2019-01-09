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
public class TestClaim {

  LocalDate date;

  public static TestClaim fromClaimData(List<ClaimData> d) {
    LocalDate date = null;

    val claimWithoutDuplicates = ClaimData.withoutDuplicates(d);

    for (val cwd : claimWithoutDuplicates) {
      if ("DATE".equals(cwd.getName())) {
        date = LocalDateTime.parse(cwd.getValue()).toLocalDate();
      }
    }
    return new TestClaim(date);
  }

}
