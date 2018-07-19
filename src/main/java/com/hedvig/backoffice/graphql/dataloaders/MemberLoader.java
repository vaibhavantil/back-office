package com.hedvig.backoffice.graphql.dataloaders;

import com.hedvig.backoffice.graphql.types.Member;

import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

@Component
public class MemberLoader extends DataLoader<String, Member> {
    public MemberLoader(MemberBatchLoader memberBatchLoader) {
        super(memberBatchLoader);
    }
}