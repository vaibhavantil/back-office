package com.hedvig.backoffice.services.payments.dto;

import lombok.Value;

@Value
public class DirectDebitStatusDTO {
  String memberId;
  boolean directDebitActivated;
}
