package com.hedvig.backoffice.services.claims.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClaimField {
    @NotNull
    private String name;

    @NotNull
    private String title;

    @NotNull
    private String type;
}
