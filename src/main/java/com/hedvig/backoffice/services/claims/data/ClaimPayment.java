package com.hedvig.backoffice.services.claims.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClaimPayment extends ClaimBackOffice {

    public Double amount;
    public String note;
    public LocalDateTime payoutDate;
    public Boolean exGratia;

}
