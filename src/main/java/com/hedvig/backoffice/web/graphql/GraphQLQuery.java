package com.hedvig.backoffice.web.graphql;

import java.time.YearMonth;
import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.web.graphql.types.Member;
import com.hedvig.backoffice.web.graphql.types.MonthlySubscription;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GraphQLQuery implements GraphQLQueryResolver {

    private final ProductPricingService productPricingService;

    public GraphQLQuery(ProductPricingService productPricingService) {
        this.productPricingService = productPricingService;
    }

    public List<MonthlySubscription> getMonthlyPayments(YearMonth month) {
        return productPricingService.getMonthlyPayments(month);
    }
    public Member getMember(String id) {
        log.info("resolving member with id: {}", id);
        return new Member(id);
    }
}
