package com.hedvig.backoffice.graphql.resolvers;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.hedvig.backoffice.graphql.dataloaders.ClaimLoader;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.Claim;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.MonthlySubscription;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import org.springframework.stereotype.Component;

@Component
public class GraphQLQuery implements GraphQLQueryResolver {

  private final ProductPricingService productPricingService;
  private final MemberLoader memberLoader;
  private final ClaimLoader claimLoader;

  public GraphQLQuery(ProductPricingService productPricingService, MemberLoader memberLoader,
      ClaimLoader claimLoader) {
    this.productPricingService = productPricingService;
    this.memberLoader = memberLoader;
    this.claimLoader = claimLoader;
  }

  public List<MonthlySubscription> monthlyPayments(YearMonth month) {

    return productPricingService.getMonthlyPayments(month).stream()
        .map(ms -> new MonthlySubscription(ms.getMemberId(), ms.getSubscription()))
        .collect(Collectors.toList());
  }

  public CompletableFuture<Member> member(String id) {
    return memberLoader.load(id);
  }

  public CompletableFuture<Claim> claim(UUID id) {
    return claimLoader.load(id);
  }
}
