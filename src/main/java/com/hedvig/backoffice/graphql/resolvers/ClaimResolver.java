package com.hedvig.backoffice.graphql.resolvers;

import java.util.concurrent.CompletableFuture;
import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.AccidentalDamageClaim;
import com.hedvig.backoffice.graphql.types.Claim;
import com.hedvig.backoffice.graphql.types.FireClaim;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.TheftClaim;
import org.springframework.stereotype.Component;
import graphql.schema.DataFetchingEnvironment;

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
    switch (claim.get_type()) {
      case "FIRE": {
        return FireClaim.fromClaimData(claim.get_claimData());
      }
      case "THEFT": {
        return TheftClaim.fromClaimData(claim.get_claimData());
      }
      case "DRULLE": {
        return AccidentalDamageClaim.fromClaimData(claim.get_claimData());
      }
    }
    throw new RuntimeException(String.format("Unsupported claim type: %s", claim.get_type()));
  }
}
