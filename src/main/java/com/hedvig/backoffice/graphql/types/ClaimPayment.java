package com.hedvig.backoffice.graphql.types;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;
import lombok.Value;

@Value
public class ClaimPayment {
  MonetaryAmount amount;
  String note;
  ClaimPaymentType type;
  Instant timestamp;
  Boolean exGratia;

  Optional<UUID> transactionId;

  public static ClaimPayment fromDto(com.hedvig.backoffice.services.claims.dto.ClaimPayment dto) {
    return new ClaimPayment(Money.of(dto.getAmount(), "SEK"), dto.getNote(),
        ClaimPaymentType.valueOf(dto.getPaymentType().toString()),
        dto.getDate().toInstant(ZoneOffset.UTC), dto.getExGratia(), dto.getTransactionId());
  }
}
