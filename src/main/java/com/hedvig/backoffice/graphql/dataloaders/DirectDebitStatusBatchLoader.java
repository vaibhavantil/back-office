package com.hedvig.backoffice.graphql.dataloaders;

import com.hedvig.backoffice.graphql.types.DirectDebitStatus;
import com.hedvig.backoffice.services.payments.PaymentService;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import org.dataloader.BatchLoader;
import org.springframework.stereotype.Component;

@Component
public class DirectDebitStatusBatchLoader implements BatchLoader<String, DirectDebitStatus> {
  private final PaymentService paymentService;

  public DirectDebitStatusBatchLoader(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @Override
  public CompletionStage<List<DirectDebitStatus>> load(List<String> keys) {
    return CompletableFuture.supplyAsync(
        () -> {
          return paymentService
              .getDirectDebitStatuses(keys)
              .stream()
              .map(dds -> new DirectDebitStatus(dds.getMemberId(), dds.isDirectDebitActivated()))
              .sorted(Comparator.comparing(item -> keys.indexOf(item.getMemberId())))
              .collect(Collectors.toList());
        });
  }
}
