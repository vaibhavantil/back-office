package com.hedvig.backoffice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimEventDTO {

    @NotNull
    private String claimId;

    @NotNull
    private Instant timestamp;

    @NotNull
    private String message;

    public ClaimEventDTO(String claimId, String message) {
        this.claimId = claimId;
        this.timestamp = new Date().toInstant();
        this.message = message;
    }
}
