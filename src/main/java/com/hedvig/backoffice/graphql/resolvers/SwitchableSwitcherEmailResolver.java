package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.SwitchableSwitcherEmail;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class SwitchableSwitcherEmailResolver implements GraphQLResolver<SwitchableSwitcherEmail> {
    private final MemberLoader memberLoader;

    public SwitchableSwitcherEmailResolver(final MemberLoader memberLoader) {
        this.memberLoader = memberLoader;
    }

    public CompletableFuture<Member> getMember(final SwitchableSwitcherEmail switchableSwitcherEmail) {
        return memberLoader.load(switchableSwitcherEmail.getMemberId());
    }
}
