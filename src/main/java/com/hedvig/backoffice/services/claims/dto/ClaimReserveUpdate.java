package com.hedvig.backoffice.services.claims.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ClaimReserveUpdate extends ClaimBackOffice {

    @NotNull
    private BigDecimal amount;

}
