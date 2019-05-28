package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.MonthlySubscription;
import com.hedvig.backoffice.graphql.types.SchedulerStatus;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class SchedulerStatusResolver implements GraphQLResolver<SchedulerStatus> {
  private final MemberLoader memberLoader;

  public SchedulerStatusResolver(MemberLoader memberLoader) {
    this.memberLoader = memberLoader;
  }

  public CompletableFuture<Member> getMember(SchedulerStatus schedulerStatus) {
    return memberLoader.load(schedulerStatus.getMemberId());
  }
}
