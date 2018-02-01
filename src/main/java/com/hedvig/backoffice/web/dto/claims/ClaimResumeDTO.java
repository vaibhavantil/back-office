package com.hedvig.backoffice.web.dto.claims;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ClaimResumeDTO {

    @NotNull
    private BigDecimal resume;

}
