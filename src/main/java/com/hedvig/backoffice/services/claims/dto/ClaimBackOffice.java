package com.hedvig.backoffice.services.claims.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClaimBackOffice {
    public String id;
    public String claimID;
    public LocalDateTime date;
    public String userId;
}
