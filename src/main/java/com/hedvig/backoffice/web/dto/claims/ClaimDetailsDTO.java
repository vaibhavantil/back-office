package com.hedvig.backoffice.web.dto.claims;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class ClaimDetailsDTO {

    @NotNull
    private Map<String, String> required;

    @NotNull
    private Map<String, String> additional;

}
