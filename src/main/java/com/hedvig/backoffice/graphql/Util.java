package com.hedvig.backoffice.graphql;

import com.hedvig.backoffice.graphql.types.ClaimTypes;
import com.hedvig.backoffice.services.claims.dto.ClaimData;

import java.util.Comparator;

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
      case FireDamageClaim:
        return "Fire Damage";
      case NotCoveredClaim:
        return "Not covered";
      case ConfirmedFraudClaim:
        return "Confirmed Fraud";
      case TestClaim:
        return "Test";
      case LiabilityClaim:
        return "Liability";
      default:
        throw new RuntimeException(String.format("Unmappable ClaimTypes.%s", type.toString()));
    }
  }

  public static ClaimTypes claimType(String claimServiceType) {
    switch (claimServiceType) {
      case "Assault":
        return ClaimTypes.AssaultClaim;
      case "Confirmed Fraud":
        return ClaimTypes.ConfirmedFraudClaim;
      case "DRULLE":
      case "Drulle - Mobile":
      case "Drulle - Other":
        return ClaimTypes.AccidentalDamageClaim;
      case "FILE":
      case "FIRE":
      case "Fire Damage":
        return ClaimTypes.FireDamageClaim;
      case "Liability":
        return ClaimTypes.LiabilityClaim;
      case "Not covered":
        return ClaimTypes.NotCoveredClaim;
      case "Test":
        return ClaimTypes.TestClaim;
      case "THEFT":
      case "Theft - Bike":
      case "Theft - Home":
      case "Theft - Other":
        return ClaimTypes.TheftClaim;
      case "Travel - Accident and Health":
        return ClaimTypes.TravelAccidentClaim;
      case "Travel - Delayed Luggage":
        return ClaimTypes.LuggageDelayClaim;
      case "Water Damage - Bathroom":
      case "Water Damage - Kitchen":
        return ClaimTypes.WaterDamageClaim;
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
