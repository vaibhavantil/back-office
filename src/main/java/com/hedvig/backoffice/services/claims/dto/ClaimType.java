package com.hedvig.backoffice.services.claims.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.List;

@Value
public class ClaimType {
    @NotNull
    private String name;

    @NotNull
    private String title;

    @NotNull
    private List<ClaimField> requiredData;

    @NotNull
    private List<ClaimField> optionalData;
}
