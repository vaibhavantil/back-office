package com.hedvig.backoffice.services.claims.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ClaimReserve {

    @NotNull
    private BigDecimal reserve;

}
