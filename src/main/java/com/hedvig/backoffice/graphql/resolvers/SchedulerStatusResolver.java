package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.GraphQLConfiguration;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.account.SchedulerStatus;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import graphql.schema.DataFetchingEnvironment;
import lombok.val;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SchedulerStatusResolver implements GraphQLResolver<SchedulerStatus> {
  // private final MemberLoader memberLoader;

  private final MemberService memberService;
  private final PersonnelService personnelService;

  public SchedulerStatusResolver(MemberLoader memberLoader, MemberService memberService, PersonnelService personnelService) {
    // this.memberLoader = memberLoader;
    this.memberService = memberService;
    this.personnelService = personnelService;
  }

  public Member getMember(SchedulerStatus schedulerStatus, DataFetchingEnvironment env) {
    // return memberLoader.load(schedulerStatus.getMemberId());
    val token = GraphQLConfiguration.getIdToken(env, personnelService);
    try {
      return Member.Companion.fromDTO(memberService.findByMemberId(schedulerStatus.getMemberId(), token));
    } catch (Exception e) {
      return new Member(schedulerStatus.getMemberId(), Instant.now(), "UNKNOWN", "UNKNOWN", "Unknown", "Unknown", "Unknown", "Unknown");
    }
  }
}
