package com.hedvig.backoffice.services.payments.dto;

import java.util.Map;
import java.util.UUID;
import lombok.Value;

@Value
public class PaymentMemberDTO {
  String id;
  Map<UUID, TransactionDTO> transactions;
  boolean directDebitMandateActive;
  String trustlyAccountNumber;
}
