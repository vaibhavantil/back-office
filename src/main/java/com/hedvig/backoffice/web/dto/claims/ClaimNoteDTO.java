package com.hedvig.backoffice.web.dto.claims;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
public class ClaimNoteDTO {

    private String id;

    private String claimId;

    @NotNull
    private String text;

    private Long adminId;

    private Instant date;

}
