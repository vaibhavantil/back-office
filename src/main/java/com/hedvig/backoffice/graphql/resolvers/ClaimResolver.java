package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.Util;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.AccidentalDamageClaim;
import com.hedvig.backoffice.graphql.types.ApplianceClaim;
import com.hedvig.backoffice.graphql.types.AssaultClaim;
import com.hedvig.backoffice.graphql.types.Claim;
import com.hedvig.backoffice.graphql.types.ConfirmedFraudClaim;
import com.hedvig.backoffice.graphql.types.FireDamageClaim;
import com.hedvig.backoffice.graphql.types.LiabilityClaim;
import com.hedvig.backoffice.graphql.types.LuggageDelayClaim;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.NotCoveredClaim;
import com.hedvig.backoffice.graphql.types.TestClaim;
import com.hedvig.backoffice.graphql.types.TheftClaim;
import com.hedvig.backoffice.graphql.types.TravelAccidentClaim;
import com.hedvig.backoffice.graphql.types.WaterDamageClaim;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ClaimResolver implements GraphQLResolver<Claim> {

  private final MemberLoader memberLoader;

  public ClaimResolver(MemberLoader memberLoader) {
    this.memberLoader = memberLoader;
  }

  public CompletableFuture<Member> getMember(Claim claim) {
    return memberLoader.load(claim.getMemberId());
  }

  public Object getType(Claim claim, DataFetchingEnvironment env) {
    if (claim.get_type() == null) {
      return null;
    }
    switch (Util.claimType(claim.get_type())) {
      case TheftClaim: {
        return TheftClaim.fromClaimData(claim.get_claimData());
      }
      case AssaultClaim: {
        return AssaultClaim.fromClaimData(claim.get_claimData());
      }
      case AccidentalDamageClaim: {
        return AccidentalDamageClaim.fromClaimData(claim.get_claimData());
      }
      case WaterDamageClaim: {
        return WaterDamageClaim.fromClaimData(claim.get_claimData());
      }
      case TravelAccidentClaim: {
        return TravelAccidentClaim.fromClaimData(claim.get_claimData());
      }
      case LuggageDelayClaim: {
        return LuggageDelayClaim.fromClaimData(claim.get_claimData());
      }
      case NotCoveredClaim: {
        return NotCoveredClaim.fromClaimData(claim.get_claimData());
      }
      case FireDamageClaim: {
        return FireDamageClaim.fromClaimData(claim.get_claimData());
      }
      case ConfirmedFraudClaim: {
        return ConfirmedFraudClaim.fromClaimData(claim.get_claimData());
      }
      case TestClaim: {
        return TestClaim.fromClaimData(claim.get_claimData());
      }
      case LiabilityClaim: {
        return LiabilityClaim.fromClaimData(claim.get_claimData());
      }
      case ApplianceClaim: {
        return ApplianceClaim.fromClaimData(claim.get_claimData());
      }
    }
    throw new RuntimeException(String.format("Unsupported claim type: %s", claim.get_type()));
  }
}
