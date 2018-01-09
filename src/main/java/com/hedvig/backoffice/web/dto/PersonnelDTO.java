package com.hedvig.backoffice.web.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class PersonnelDTO {

    @NotNull
    private String email;

    @NotNull
    private String password;

}
