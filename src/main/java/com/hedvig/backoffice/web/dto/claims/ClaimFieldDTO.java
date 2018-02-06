package com.hedvig.backoffice.web.dto.claims;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
class ClaimFieldDTO {
    @NotNull
    private String name;

    @NotNull
    private String title;

    @NotNull
    private String type;
}
