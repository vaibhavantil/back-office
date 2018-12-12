package com.hedvig.backoffice.graphql;

import java.util.Comparator;
import com.hedvig.backoffice.graphql.types.ClaimTypes;
import com.hedvig.backoffice.services.claims.dto.ClaimData;

public class Util {
  public static String claimServiceType(ClaimTypes type) {
    switch (type) {
      case TheftClaim:
        return "THEFT";
      case AssaultClaim:
        return "Assault";
      case AccidentalDamageClaim:
        return "DRULLE";
      case WaterDamageClaim:
        return "Water Damage - Bathroom";
      case TravelAccidentClaim:
        return "Travel - Accident and Health";
      case LuggageDelayClaim:
        return "Travel - Delayed Luggage";
      case NotCoveredClaim:
        return "Not Covered";
      default:
        throw new RuntimeException(String.format("Unmappable ClaimTypes.%s", type.toString()));
    }
  }

  public static ClaimTypes claimType(String claimServiceType) {
    switch (claimServiceType) {
      case "THEFT":
      case "Theft - Other":
      case "Theft - Home":
        return ClaimTypes.TheftClaim;
      case "Assault":
        return ClaimTypes.AssaultClaim;
      case "DRULLE":
      case "Drulle - Mobile":
      case "Drulle - Other":
        return ClaimTypes.AccidentalDamageClaim;
      case "Water Damage - Kitchen":
      case "Water Damage - Bathroom":
        return ClaimTypes.WaterDamageClaim;
      case "Travel - Accident and Health":
        return ClaimTypes.TravelAccidentClaim;
      case "Travel - Delayed Luggage":
        return ClaimTypes.LuggageDelayClaim;
      case "Not Covered":
        return ClaimTypes.NotCoveredClaim;
      default:
        throw new RuntimeException(
            String.format("Unmappable ClaimService ClaimType: %s", claimServiceType));
    }
  }

  public static Comparator<ClaimData> sortedByDateDescComparator = new Comparator<ClaimData>() {

    @Override
    public int compare(ClaimData o1, ClaimData o2) {
      return o2.getDate().compareTo(o1.getDate());
    }
  };
}
