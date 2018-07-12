package com.hedvig.backoffice.web.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.web.graphql.types.Member;
import com.hedvig.backoffice.web.graphql.types.MonthlySubscription;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class MonthlySubscriptionResolver implements GraphQLResolver<MonthlySubscription> {
    public Member getMember(MonthlySubscription monthlySubscription) {
        log.info("Resolving member with id: {}", monthlySubscription.getMemberId());
        return new Member(monthlySubscription.getMemberId());
    }
}