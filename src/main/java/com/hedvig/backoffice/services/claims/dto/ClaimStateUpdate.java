package com.hedvig.backoffice.services.claims.dto;

import com.hedvig.backoffice.services.claims.ClaimState;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClaimStateUpdate extends ClaimBackOffice {

  @NotNull private ClaimState state;
}
