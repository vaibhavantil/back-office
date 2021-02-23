package com.hedvig.backoffice.services.claims.dto;

import com.hedvig.backoffice.graphql.Util;
import lombok.Data;
import lombok.val;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ClaimData extends ClaimBackOffice {

    public String type;
    public String name;
    public String title;
    public Boolean received;
    public String value;

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
