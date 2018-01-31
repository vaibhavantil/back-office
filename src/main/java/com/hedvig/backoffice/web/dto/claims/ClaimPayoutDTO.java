package com.hedvig.backoffice.web.dto.claims;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
public class ClaimPayoutDTO {

    private String id;
    private String claimId;
    private Instant date;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String noteId;

    @NotNull
    private boolean exg;

}
