package com.hedvig.backoffice.graphql.resolvers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.graphql.dataloaders.DirectDebitStatusLoader;
import com.hedvig.backoffice.graphql.types.DirectDebitStatus;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.Transaction;

import org.springframework.stereotype.Component;

@Component
public class MemberResolver implements GraphQLResolver<Member> {
    private final PaymentService paymentService;
	private final DirectDebitStatusLoader directDebitStatusLoader;

	public MemberResolver(PaymentService paymentService, DirectDebitStatusLoader directDebitStatusLoader) {
        this.paymentService = paymentService;
        this.directDebitStatusLoader = directDebitStatusLoader;
    }

    public List<Transaction> getTransactions(Member member) {
        return paymentService
            .getTransactionsByMemberId(member.getMemberId())
            .stream()
            .map(transactionDTO -> new Transaction(
                transactionDTO.getId(),
                transactionDTO.getAmount(),
                transactionDTO.getTimestamp(),
                transactionDTO.getType(),
                transactionDTO.getStatus()
            ))
            .collect(Collectors.toList());
    }

    public CompletableFuture<DirectDebitStatus> getDirectDebitStatus(Member member) {
        return directDebitStatusLoader
            .load(member.getMemberId());
    }
}
