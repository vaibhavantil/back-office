package com.hedvig.backoffice.web.dto.claims;

import com.hedvig.backoffice.web.dto.claims.fields.ClaimField;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.List;

@Value
public class ClaimTypeDTO {
    @NotNull
    private String name;

    @NotNull
    private String title;

    @NotNull
    private List<ClaimField> required;

    @NotNull
    private List<ClaimField> additional;
}
