package com.hedvig.backoffice.services.claims.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ClaimPayment extends ClaimBackOffice {

    @NotNull
    public BigDecimal amount;

    public String note;

    public LocalDateTime payoutDate;

    @NotNull
    public Boolean exGratia;

}
