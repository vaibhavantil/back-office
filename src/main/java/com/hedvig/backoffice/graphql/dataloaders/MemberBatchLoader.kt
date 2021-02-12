package com.hedvig.backoffice.graphql.dataloaders

import com.hedvig.backoffice.graphql.types.Member
import com.hedvig.backoffice.services.members.MemberService
import org.dataloader.BatchLoader
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Component
class MemberBatchLoader(private val memberService: MemberService) : BatchLoader<String, Member> {
    override fun load(keys: List<String>): CompletionStage<List<Member>> = CompletableFuture.supplyAsync {
        memberService.getMembersByIds(keys).map((Member)::fromDTO)
    }
}
