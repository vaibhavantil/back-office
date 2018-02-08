package com.hedvig.backoffice.web.dto.claims.fields;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class ClaimDateField extends ClaimField {

    @NotNull
    private Instant value;

}
