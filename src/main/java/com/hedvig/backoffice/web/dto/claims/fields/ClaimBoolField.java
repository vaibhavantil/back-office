package com.hedvig.backoffice.web.dto.claims.fields;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClaimBoolField extends ClaimField {

    @NotNull
    private boolean value;

}
