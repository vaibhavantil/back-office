package com.hedvig.backoffice.services.claims.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClaimPayment extends ClaimBackOffice {

  @NotNull
  public BigDecimal amount;

  public String note;

  public LocalDateTime payoutDate;

  @NotNull
  public Boolean exGratia;

  ClaimPaymentType paymentType;

  Optional<UUID> transactionId;
}
