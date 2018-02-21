package com.hedvig.backoffice.services.claims.dto;

import com.hedvig.backoffice.services.claims.ClaimState;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClaimStateUpdate extends ClaimBackOffice {

    @NotNull
    private ClaimState state;

}
