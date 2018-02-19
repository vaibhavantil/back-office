package com.hedvig.backoffice.services.claims.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClaimBackOffice {

    protected String id;
    protected String claimId;
    protected LocalDateTime date;
    protected String userId;

}
