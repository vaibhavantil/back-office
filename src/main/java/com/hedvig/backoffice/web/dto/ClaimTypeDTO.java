package com.hedvig.backoffice.web.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Value
public class ClaimTypeDTO {

    @NotNull
    private String name;

    @NotNull
    private Map<String, Boolean> required;

    @NotNull
    private Map<String, Boolean> additional;

}
