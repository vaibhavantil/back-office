package com.hedvig.backoffice.web;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.google.common.collect.Lists;
import com.hedvig.backoffice.web.dto.MonthlySubscription;

import org.springframework.stereotype.Component;

@Component
public class GraphQL implements GraphQLQueryResolver {
    public List<MonthlySubscription> getMonthlyPayments(int year, int month) {
        return Lists.newArrayList(new MonthlySubscription("12345", true, 100));
    }
}
