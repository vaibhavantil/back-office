package com.hedvig.backoffice.services.claims.dto;

import com.hedvig.backoffice.services.claims.ClaimStates;
import lombok.Data;

@Data
public class ClaimState extends ClaimBackOffice {

    private ClaimStates state;

}
