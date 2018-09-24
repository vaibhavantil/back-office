package com.hedvig.backoffice.graphql.resolvers;

import java.util.concurrent.CompletableFuture;
import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Claim;
import com.hedvig.backoffice.graphql.types.Member;
import org.springframework.stereotype.Component;

@Component
public class ClaimResolver implements GraphQLResolver<Claim> {

  private final MemberLoader memberLoader;

  public ClaimResolver(MemberLoader memberLoader) {
    this.memberLoader = memberLoader;
  }

  public CompletableFuture<Member> getMember(Claim claim) {
    return memberLoader.load(claim.getMemberId());
  }
}
