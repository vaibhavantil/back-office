package com.hedvig.backoffice.graphql.types;

import java.util.Optional;
import java.util.UUID;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;
import lombok.Value;

@Value
public class ClaimPayment {
  MonetaryAmount amount;
  String note;
  String type;

  Optional<UUID> transactionId;

  public static ClaimPayment fromDto(com.hedvig.backoffice.services.claims.dto.ClaimPayment dto) {
    return new ClaimPayment(Money.of(dto.getAmount(), "SEK"), dto.getNote(),
        dto.getPaymentType().toString(), dto.getTransactionId());
  }
}
