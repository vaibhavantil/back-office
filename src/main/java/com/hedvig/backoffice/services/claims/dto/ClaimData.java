package com.hedvig.backoffice.services.claims.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.val;
import com.hedvig.backoffice.graphql.Util;

@Data
public class ClaimData extends ClaimBackOffice {

  private String type;
  private String name;
  private String title;
  private Boolean received;
  private String value;

  public static List<ClaimData> withoutDuplicates(List<ClaimData> claimData) {
    return claimData.stream().filter(cd -> {
      val type = cd.getName();
      val itemsWithType = claimData.stream().filter(c -> c.getName().equals(type))
          .sorted(Util.sortedByDateDescComparator).collect(Collectors.toList());
      if (itemsWithType.size() == 1) {
        return true;
      }

      if (itemsWithType.size() > 1) {
        return cd.getId().equals(itemsWithType.get(0).getId());
      }
      throw new RuntimeException("Invalid invariant");
    }).collect(Collectors.toList());
  }
}
