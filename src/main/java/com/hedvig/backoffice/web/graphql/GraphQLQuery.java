package com.hedvig.backoffice.web.graphql;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.google.common.collect.Lists;
import com.hedvig.backoffice.web.graphql.types.Member;
import com.hedvig.backoffice.web.graphql.types.MonthlySubscription;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GraphQLQuery implements GraphQLQueryResolver {
    public List<MonthlySubscription> getMonthlyPayments(int year, int month) {
        log.info("resolving monthly payments for year: {}, month: {}", year, month);
        return Lists.newArrayList(new MonthlySubscription("12345", true, 100), new MonthlySubscription("23456", false, 200));
    }
    public Member getMember(String id) {
        log.info("resolving member with id: {}", id);
        return new Member(id);
    }
}
