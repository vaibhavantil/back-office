package com.hedvig.backoffice.graphql.dataloaders;

import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.services.members.MemberService;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import org.dataloader.BatchLoader;
import org.springframework.stereotype.Component;

@Component
public class MemberBatchLoader implements BatchLoader<String, Member> {
  private final MemberService memberService;

  public MemberBatchLoader(MemberService memberService) {
    this.memberService = memberService;
  }

  @Override
  public CompletionStage<List<Member>> load(List<String> keys) {
    return CompletableFuture.supplyAsync(
        () -> {
          return memberService
              .getMembersByIds(keys)
              .stream()
              .map(m -> new Member(m.getMemberId().toString(), m.getFirstName(), m.getLastName()))
              .sorted(Comparator.comparing(item -> keys.indexOf(item.getMemberId())))
              .collect(Collectors.toList());
        });
  }
}
