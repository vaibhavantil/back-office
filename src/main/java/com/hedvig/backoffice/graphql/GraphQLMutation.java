package com.hedvig.backoffice.graphql;

import java.util.concurrent.CompletableFuture;

import javax.money.MonetaryAmount;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.services.payments.PaymentService;

import org.springframework.stereotype.Component;

@Component
public class GraphQLMutation implements GraphQLMutationResolver {
    private MemberLoader memberLoader;
	private PaymentService paymentService;

	public GraphQLMutation(PaymentService paymentService, MemberLoader memberLoader) {
        this.paymentService = paymentService;
        this.memberLoader = memberLoader;
    }

    public CompletableFuture<Member> chargeMember(String id, MonetaryAmount amount) {
        paymentService.chargeMember(id, amount);
        return memberLoader.load(id);
    }
}
