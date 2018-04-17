package com.hedvig.backoffice.services.claims.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ClaimBackOffice {

    protected String id;

    protected String claimID;

    protected LocalDateTime date;

    protected String userId;

}
