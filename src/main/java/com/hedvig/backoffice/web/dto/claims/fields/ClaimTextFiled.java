package com.hedvig.backoffice.web.dto.claims.fields;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClaimTextFiled extends ClaimField {

    @NotNull
    private String value;

}
