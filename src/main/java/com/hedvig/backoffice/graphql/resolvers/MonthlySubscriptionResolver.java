package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.MonthlySubscription;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Component;

@Component
public class MonthlySubscriptionResolver implements GraphQLResolver<MonthlySubscription> {
  private final MemberLoader memberLoader;

  public MonthlySubscriptionResolver(MemberLoader memberLoader) {
    this.memberLoader = memberLoader;
  }

  public CompletableFuture<Member> getMember(MonthlySubscription monthlySubscription) {
    return memberLoader.load(monthlySubscription.getMemberId());
  }
}
