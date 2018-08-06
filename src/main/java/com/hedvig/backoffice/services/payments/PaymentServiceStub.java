package com.hedvig.backoffice.services.payments;

import com.google.common.collect.Lists;
import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;
import com.hedvig.backoffice.services.payments.dto.Transaction;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.money.MonetaryAmount;
import org.apache.commons.lang3.RandomUtils;
import org.javamoney.moneta.Money;

public class PaymentServiceStub implements PaymentService {

  @Override
  public List<Transaction> getTransactionsByMemberId(String memberId) {
    return Lists.newArrayList(
        new Transaction(
            UUID.randomUUID(),
            Money.of(100, "SEK"),
            Instant.now().minusSeconds(100),
            "CHARGE",
            "COMPLETED"),
        new Transaction(
            UUID.randomUUID(), Money.of(200, "SEK"), Instant.now(), "CHARGE", "INITIATED"));
  }

  @Override
  public Boolean hasDirectDebitActivated(String memberId) {
    return true;
  }

  @Override
  public List<DirectDebitStatusDTO> getDirectDebitStatuses(List<String> memberIds) {
    return memberIds
        .stream()
        .map(id -> new DirectDebitStatusDTO(id, RandomUtils.nextBoolean()))
        .collect(Collectors.toList());
  }

  @Override
  public void chargeMember(String memberId, MonetaryAmount amount) {}
}
