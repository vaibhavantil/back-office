package com.hedvig.backoffice.services.claims.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClaimPayment extends ClaimBackOffice {

    public Double amount;
    public String note;
    public LocalDateTime payoutDate;
    public Boolean exGratia;

}
