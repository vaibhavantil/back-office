package com.hedvig.backoffice.graphql.types;

import com.hedvig.backoffice.services.members.dto.PaymentDefaultDTO;
import lombok.Value;

import javax.money.MonetaryAmount;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Value
public class Debt {
  List<PaymentDefaultDTO> paymentDefaults;
  LocalDate debtDate;
  MonetaryAmount totalAmountPublicDebt;
  Integer numberPublicDebts;
  MonetaryAmount totalAmountPrivateDebt;
  Integer numberPrivateDebts;
  MonetaryAmount totalAmountDebt;
  Instant checkedAt;
  LocalDateTime fromDateTime;
}
