package com.hedvig.backoffice.graphql.dataloaders;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.graphql.types.Member;

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
        return CompletableFuture.supplyAsync(() -> {
            return memberService
                .getMembersByIds(keys)
                .stream()
                .map(m -> new Member(m.getMemberId().toString(), m.getFirstName(), m.getLastName()))
                .collect(Collectors.toList());
        });
	}
}
