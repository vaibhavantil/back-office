package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.MonthlyBordereau;
import java.util.concurrent.CompletableFuture;

public class MonthlyBordereauResolver implements GraphQLResolver<MonthlyBordereau> {

  private final MemberLoader memberLoader;

  public MonthlyBordereauResolver(MemberLoader memberLoader) {
    this.memberLoader = memberLoader;
  }

  public CompletableFuture<Member> getMember(MonthlyBordereau monthlyBordereau) {
    return memberLoader.load(monthlyBordereau.getMemberId());
  }
}
