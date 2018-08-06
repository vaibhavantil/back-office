package com.hedvig.backoffice.services.claims.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClaimReserveUpdate extends ClaimBackOffice {

  @NotNull private BigDecimal amount;
}
