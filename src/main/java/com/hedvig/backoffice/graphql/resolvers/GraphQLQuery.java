package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.MonthlySubscription;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import java.time.YearMonth;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class GraphQLQuery implements GraphQLQueryResolver {

  private final ProductPricingService productPricingService;
  private MemberLoader memberLoader;

  public GraphQLQuery(ProductPricingService productPricingService, MemberLoader memberLoader) {
    this.productPricingService = productPricingService;
    this.memberLoader = memberLoader;
  }

  public List<MonthlySubscription> getMonthlyPayments(YearMonth month) {

    return productPricingService
        .getMonthlyPayments(month)
        .stream()
        .map(ms -> new MonthlySubscription(ms.getMemberId(), ms.getSubscription()))
        .collect(Collectors.toList());
  }

  public CompletableFuture<Member> getMember(String id) {
    return memberLoader.load(id);
  }
}
